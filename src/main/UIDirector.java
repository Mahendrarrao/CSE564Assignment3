package main;

import java.awt.BorderLayout;

public class UIDirector {
	private UIBuilder uiBuilder;
	
	public UIDirector(UIBuilder uibuilder) {
		this.uiBuilder = uibuilder;
	}
	
	public View getView() {
		return uiBuilder.getView();
	}
	
	public void makeUI() {
		this.uiBuilder.buildTopPanel();
		this.uiBuilder.buildDrawAndPredictionArea();
		this.uiBuilder.getView().getMainFrame().add(this.uiBuilder.getView().getMainPanel(), BorderLayout.CENTER);
		this.uiBuilder.getView().getMainFrame().setVisible(true);
	}
}
