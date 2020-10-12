package main;

public class NewUIBuilder implements UIBuilder
{
	private View view;
	
	public NewUIBuilder() throws Exception {
		view = new View();
	}

	@Override
	public void buildDrawAndPredictionArea() {
		
		view.addDrawAreaAndPredictionArea();
		
	}

	@Override
	public void buildTopPanel() {
		
		view.addTopPanel();
		
	}

	@Override
	public void buildMainFrame() {
		view.createMainFrame();
	}

	@Override
	public View getView() {
		return view;
	}

}
