package edu.cuhk.cubt.service;

import edu.cuhk.cubt.ui.CubtService;

public interface IServiceMonitor {
	public void start(CubtService service);
	public void stop(CubtService service);
}
