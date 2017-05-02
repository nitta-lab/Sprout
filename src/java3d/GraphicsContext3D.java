package java3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import framework.model3D.Position3D;

import android.opengl.GLU;
import android.opengl.GLUtils;

public class GraphicsContext3D {
	private GL10 gl;
	private Appearance appearance = null;
	private boolean bFixAspect = false;
	private float aspect;
	private HashMap<Texture, Integer> textureRegistry = new HashMap<Texture, Integer>();
	private HashMap<Light, Integer> lightRegistry = new HashMap<Light, Integer>();
	
	public GraphicsContext3D(GL10 gl) {
		init(gl);
	}

	public GraphicsContext3D setGL10(GL10 gl) {
		if (this.gl != gl) {
			init(gl);
		}
		return this;
	}
	
	public void fixAspect(float aspect) {
		this.bFixAspect = true;
		this.aspect = aspect;
	}
	
	public void init(GL10 gl) {
		this.gl = gl;
		// �f�v�X�o�b�t�@�̃e�X�g�@�\��L���ɂ���
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// �A�ʏ����̓����ݒ�
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glDepthMask(true);
		
		// ���C�g��L���ɂ���
		gl.glEnable(GL10.GL_LIGHTING);
		// �ǂ̌������g�p���邩�w��
		gl.glEnable(GL10.GL_LIGHT0);
		
		gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
	    gl.glClearDepthf(1.0f);
	}
	
	public void update(int width, int height, float fovx, float zNear, float zFar,boolean fParallel) {
		setGL10(gl);
		if (!bFixAspect) {
			aspect = (float)width / (float)height;
		}
		// �r���[�|�[�g�̐ݒ�
		gl.glViewport(0, 0, width, height);
		
		// �J�����̐ݒ�
		gl.glMatrixMode(GL10.GL_PROJECTION);	// �ˉe�ϊ�
		gl.glLoadIdentity();					// ���W�̏�����
		// ��p�̐ݒ�
		float fovy = (float)(Math.atan(Math.tan(fovx / 2.0) / aspect) / Math.PI * 360.0f);
		if(!fParallel){
			GLU.gluPerspective(gl,
					fovy,  //Y�����̉�p
					aspect, //�A�X�y�N�g��
					zNear,  //�j�A�N���b�v
					zFar);//�t�@�[�N���b�v
		}else{
	        float top = zNear * (float) Math.tan(fovy * (Math.PI / 360.0));
	        float bottom = -top;
	        float left = bottom * aspect;
	        float right = top * aspect;
	        gl.glOrthof(left, right, bottom, top, zNear, zFar);
		}
	}

	public void update(float fovx, float zNear, float zFar, Position3D eye, Position3D center, Vector3d up, boolean fParallel) {
		// �\����ʂƃf�v�X�o�b�t�@�̃N���A
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// �J�����̐ݒ�
		gl.glMatrixMode(GL10.GL_PROJECTION);	// �ˉe�ϊ�
		gl.glLoadIdentity();					// ���W�̏�����
		// ��p�̐ݒ�
		float fovy = (float)(Math.atan(Math.tan(fovx / 2.0) / aspect) / Math.PI * 360.0f);
		if (!fParallel) {
	        GLU.gluPerspective(gl,
	        		fovy,  //Y�����̉�p
	                aspect, //�A�X�y�N�g��
	                zNear,  //�j�A�N���b�v
	                zFar);//�t�@�[�N���b�v		
		} else {
	        float top = zNear * (float) Math.tan(fovy * (Math.PI / 360.0));
	        float bottom = -top;
	        float left = bottom * aspect;
	        float right = top * aspect;
	        gl.glOrthof(left, right, bottom, top, zNear, zFar);
		}
		// ���f���r���[�s��̎w��
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// ���W�̏�����
		gl.glLoadIdentity();
		
		// �J�����O���p�����[�^�̐ݒ�
        GLU.gluLookAt(gl,
        		(float)eye.getX(), (float)eye.getY(), (float)eye.getZ(),
        		(float)center.getX(), (float)center.getY(), (float)center.getZ(),
        		(float)up.getX(), (float)up.getY(), (float)up.getZ());
	}
	
	public void setLight(Light l, int i) {
		lightRegistry.put(l, i);
		Color3f c = l.getColor();
		float color[] = {c.r, c.g, c.b, 1.0f};
		gl.glEnable(GL10.GL_LIGHT0 + i);
		if (l instanceof AmbientLight) {
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_AMBIENT, color, 0);
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_DIFFUSE, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_SPECULAR, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
		} else {
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_DIFFUSE, color, 0);
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
		}
	}
	
	public void updateLightState(Light l) {
		Integer i = lightRegistry.get(l);
		if (i == null) {
			// Object3D�����ɔz�u����Ă�������ɑ΂��ẮA����������setLight()���Ă΂�Ȃ�����
			i = lightRegistry.size();
			setLight(l, i);
		}
		if (l instanceof DirectionalLight) {
			Vector3f v = ((DirectionalLight)l).getDirection();
			float direction[] = {-v.x, -v.y, -v.z, 0.0f};
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_POSITION, direction, 0);
		} else if (l instanceof PointLight) {
			Point3f p = ((PointLight)l).getPosition();
			float position[] = {p.x, p.y, p.z, 1.0f};
			gl.glLightfv(GL10.GL_LIGHT0 + i, GL10.GL_POSITION, position, 0);			
		}
	}

	public void pushMatrix() {
		gl.glPushMatrix();
	}

	public void popMatrix() {
		gl.glPopMatrix();
	}

	public void multMatrix(Transform3D transform) {
		float m[] = transform.getMatrix();
		gl.glMultMatrixf(m, 0);
	}
	
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		Material material = appearance.getMaterial();
		if (material != null) {
			// �\�ʑ����̐ݒ�
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, material.diffuse, 0);
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, material.ambient, 0);
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, material.specular, 0);
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_EMISSION, material.emissive, 0);
			gl.glMaterialf(GL10.GL_FRONT, GL10.GL_SHININESS, material.shininess);
		}
		if (appearance.getTextureUnitCount() == 0) {
			// �e�N�X�`�����j�b�g���g���Ă��Ȃ��ꍇ�i�ʏ�̃e�N�X�`���̏ꍇ�j
			Texture tex = appearance.getTexture();
			if (tex != null) registerTexture(tex);
		} else {
			// �e�N�X�`�����j�b�g���g���Ă���ꍇ
			for (int n = 0; n < appearance.getTextureUnitCount(); n++) {
				TextureUnitState tus = appearance.getTextureUnitState(n);
				Texture tex = tus.getTexture();
				if (tex != null) {
					gl.glActiveTexture(GL10.GL_TEXTURE1 + n);
					registerTexture(tex);
				}
			}
		}
	}

	private void registerTexture(Texture tex) {
		if (textureRegistry.get(tex) == null) {
			// �e�N�X�`���̓o�^
			int[] textureId = new int[1];
			gl.glGenTextures(1, textureId, 0);
			if (tex instanceof TextureCubeMap) {
				// �����̃}�b�v�̏ꍇ(GL10�ł͑Ή����Ă��Ȃ�)
			} else {
				// �ʏ�̏ꍇ
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
				ImageComponent[] imageComponents = tex.getImages(); 
				for (int level = 0; level < imageComponents.length; level++) {
					GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, ((ImageComponent2D)imageComponents[level]).getBitmap(), 0);
//							((ImageComponent2D)imageComponents[i]).getBitmap().recycle();
					gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
				}
			}
			textureRegistry.put(tex, textureId[0]);
		}
	}
	
	public void draw(Shape3D node) {
		if (node == null) return;
		setAppearance(node.getAppearance());		
		draw(node.getGeometry());
	}

	public void draw(Geometry g) {
		if (g == null) return;
		if (appearance.getTextureUnitCount() == 0) {
			// �e�N�X�`�����j�b�g���g���Ă��Ȃ��ꍇ�i�ʏ�̃e�N�X�`���̏ꍇ�j
			Texture tex = appearance.getTexture();
			if (tex != null) {
				gl.glEnable(GL10.GL_TEXTURE_2D);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureRegistry.get(tex));		// �e�N�X�`�����o�^����Ă��邱�Ƃ�O��
				TextureAttributes ta = appearance.getTextureAttributes();
				if (ta != null) setTextureAttributes(ta);
			}
		} else {
			// �e�N�X�`�����j�b�g���g���Ă���ꍇ
			for (int n = 0; n < appearance.getTextureUnitCount(); n++) {
				TextureUnitState tus = appearance.getTextureUnitState(n);
				Texture tex = tus.getTexture();
				if (tex != null) {
					gl.glActiveTexture(GL10.GL_TEXTURE0 + n);
					gl.glEnable(GL10.GL_TEXTURE_2D);
					gl.glBindTexture(GL10.GL_TEXTURE_2D, textureRegistry.get(tex));		// �e�N�X�`�����o�^����Ă��邱�Ƃ�O��
					TextureAttributes ta = tus.getTextureAttributes();
					if (ta != null) setTextureAttributes(ta);
				}
			}			
		}
		if (g instanceof GeometryArray) {
			// �o�b�t�@�̐ݒ�
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			FloatBuffer vertexBuffer = ((GeometryArray)g).getVertexBuffer();
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);			
			if ((((GeometryArray) g).getVertexFormat() & GeometryArray.NORMALS) != 0) {
				gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
				FloatBuffer normalBuffer = ((GeometryArray)g).getNormalBuffer();
				gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
			}
			if ((((GeometryArray) g).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_2) != 0) {
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				FloatBuffer uvBuffer = ((GeometryArray)g).getUVBuffer();
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvBuffer);
			} else if ((((GeometryArray) g).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_3) != 0) {
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				FloatBuffer uvBuffer = ((GeometryArray)g).getUVBuffer();
				gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, uvBuffer);
			} else if ((((GeometryArray) g).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_4) != 0) {
				gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				FloatBuffer uvBuffer = ((GeometryArray)g).getUVBuffer();
				gl.glTexCoordPointer(4, GL10.GL_FLOAT, 0, uvBuffer);
			}
			
			// �W�I���g���̕`��
			if (g instanceof TriangleArray) {
				TriangleArray ta = (TriangleArray)g;
				int vertexCount = ta.getVertexCount();
				gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexCount);			
			} else if (g instanceof TriangleFanArray) {
				TriangleFanArray ta = (TriangleFanArray)g;
				int start = 0;
				int[] stripVertexCounts = new int[ta.getNumStrips()];
				ta.getStripVertexCounts(stripVertexCounts);
				for (int n = 0; n < ta.getNumStrips(); n++) {
					int vertexCount = stripVertexCounts[n];
					gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, start, vertexCount);
					start += vertexCount;
				}
			} else if (g instanceof TriangleStripArray) {
				TriangleStripArray ta = (TriangleStripArray)g;
				int start = 0;
				int[] stripVertexCounts = new int[ta.getNumStrips()];
				ta.getStripVertexCounts(stripVertexCounts);
				for (int n = 0; n < ta.getNumStrips(); n++) {
					int vertexCount = stripVertexCounts[n];
					gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, start, vertexCount);			
					start += vertexCount;
				}
			} else if (g instanceof IndexedTriangleArray) {
				IndexedTriangleArray ita = (IndexedTriangleArray)g;
				int vertexCount = ((IndexedTriangleArray)g).getIndexCount();
				gl.glDrawElements(GL10.GL_TRIANGLES, vertexCount, GL10.GL_UNSIGNED_SHORT, ita.getCoordinateIndexBuffer());
			} else if (g instanceof IndexedTriangleFanArray) {
				IndexedGeometryStripArray igsa = (IndexedGeometryStripArray)g;
				ShortBuffer indexBuffer = igsa.getCoordinateIndexBuffer();
				int start = 0;
				int[] stripIndexCounts = new int[igsa.getNumStrips()];
				igsa.getStripIndexCounts(stripIndexCounts);
				for (int n = 0; n < igsa.getNumStrips(); n++) {
					int vertexCount = stripIndexCounts[n];
					indexBuffer.position(start);
					gl.glDrawElements(GL10.GL_TRIANGLE_FAN, vertexCount, GL10.GL_UNSIGNED_SHORT, indexBuffer);
					start += vertexCount;
				}
			} else if (g instanceof IndexedTriangleStripArray) {
				IndexedGeometryStripArray igsa = (IndexedGeometryStripArray)g;
				ShortBuffer indexBuffer = igsa.getCoordinateIndexBuffer();
				int start = 0;
				int[] stripIndexCounts = new int[igsa.getNumStrips()];
				igsa.getStripIndexCounts(stripIndexCounts);
				for (int n = 0; n < igsa.getNumStrips(); n++) {
					int vertexCount = stripIndexCounts[n];
					indexBuffer.position(start);
					gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, vertexCount, GL10.GL_UNSIGNED_SHORT, indexBuffer);
					start += vertexCount;
				}
			}
			
			if ((((GeometryArray) g).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_2) != 0) {
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			} else if ((((GeometryArray) g).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_3) != 0) {
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			} else if ((((GeometryArray) g).getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_4) != 0) {
				gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			}
			if ((((GeometryArray) g).getVertexFormat() & GeometryArray.NORMALS) != 0) {
				gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			}
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
		if (appearance.getTextureUnitCount() == 0) {
			// �e�N�X�`�����j�b�g���g���Ă��Ȃ��ꍇ�i�ʏ�̃e�N�X�`���̏ꍇ�j
			if (appearance.getTexture() != null) {
				gl.glDisable(GL10.GL_TEXTURE_2D);
			}
		} else {
			// �e�N�X�`�����j�b�g���g���Ă���ꍇ
			for (int n = 0; n < appearance.getTextureUnitCount(); n++) {
				gl.glActiveTexture(GL10.GL_TEXTURE0 + n);
				gl.glDisable(GL10.GL_TEXTURE_2D);				
			}			
			gl.glActiveTexture(GL10.GL_TEXTURE0);
		}
	}

	private void setTextureAttributes(TextureAttributes ta) {
		int textureMode = ta.getTextureMode();
		switch (textureMode) {
		case TextureAttributes.REPLACE:
			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
			break;
		case TextureAttributes.BLEND:
			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);
			break;
//		case TextureAttributes.COMBINE:
//			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_COMBINE);
//			break;
		case TextureAttributes.MODULATE:
			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
			break;
		case TextureAttributes.DECAL:
			gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_DECAL);
			break;
		}
//		int perspCorrectionMode = ta.getPerspectiveCorrectionMode();
//		switch (perspCorrectionMode) {
//		case TextureAttributes.NICEST:
//			break;
//		case TextureAttributes.FASTEST:
//			break;
//		}
	}
}
