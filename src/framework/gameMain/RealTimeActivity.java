package framework.gameMain;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class RealTimeActivity extends Activity implements Runnable {
	//インターバル確認用変数
	private long interval = 15L;
	private long prevTime = 0L;
	
	private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);
	private boolean fixedInterval;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}	
	
	protected void start(long interval){
		this.interval = interval;
		this.fixedInterval = false;
		schedule.scheduleWithFixedDelay(this, interval, interval, TimeUnit.MILLISECONDS);
	}
	
	protected void start(long delay, long interval){
		this.interval = interval;
		this.fixedInterval = false;
		schedule.scheduleWithFixedDelay(this, delay, interval, TimeUnit.MILLISECONDS);
	}
	
	protected void start(long delay, long interval, boolean fixedInterval){
		this.interval = interval;
		this.fixedInterval = fixedInterval;
		schedule.scheduleWithFixedDelay(this, delay, interval, TimeUnit.MILLISECONDS);
	}
	
	protected void stop() {
		schedule.shutdown();
	}
	
	//繰り返し実行される部分
	public void run(){
		long interval;
		if (prevTime == 0L || fixedInterval) {
			interval = this.interval;
			prevTime = System.currentTimeMillis(); 
		} else {
			long curTime = System.currentTimeMillis();
			interval = curTime - prevTime;
			prevTime = curTime;
		}
		update(interval);
	}
	
	//intervalミリ秒のインターバルをおいて定期実行
	protected abstract void update(long interval);
}
