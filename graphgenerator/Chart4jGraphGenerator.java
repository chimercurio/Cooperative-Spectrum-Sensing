package graphgenerator;

import static com.googlecode.charts4j.Color.*;

import java.awt.BorderLayout;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;

import signalprocessing.SignalProcessor;

public class Chart4jGraphGenerator implements GraphGenerator {
	
	/**
	 * Metodo per la creazione del grafico SNR-Detection
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @throws IOException 
	 **/

	public  void drawSNRtoDetectionGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup) throws IOException {
		ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			Line line=Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("% of Detection", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		// Mostro il grafico tramite java swing
		displayUrlString(chart.toURLString());
	}


	/**
	 * Metodo per la creazione del grafico SNR-Detection e il salvataggio al path specificato
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @param path Destinazione in cui salvare l'immagine
	 * @throws IOException 
	 **/

	public  void drawAndSaveSNRtoDetectionGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path) throws IOException {
		ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			Line line=Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("% of Detection", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		String imageUrl = chart.toURLString();
		saveImage(imageUrl, path);

	}


	/**
	 * Metodo per la creazione del grafico % Utenti Malevoli-Detection
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @throws IOException 
	 **/

	public  void drawMaliciousUsersToDetectionGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup) throws IOException {
		ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			Line line=Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(0,90);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("% of Malicious Users", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("% of Detection", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		// Mostro il grafico tramite java swing
		displayUrlString(chart.toURLString());
	}

	/**
	 * Metodo per la creazione del grafico % Utenti Malevoli-Detection e salvataggio su path specificata
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @param path Destinazione in cui salvare l'immagine
	 * @throws IOException 
	 **/

	public  void drawAndSaveMaliciousUsersToDetectionGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path) throws IOException {
		ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			Line line=Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 100);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(0,90);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("% of Malicious Users", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("% of Detection", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		String imageUrl = chart.toURLString();
		saveImage(imageUrl, path);
	}




	/** 
	 * Metodo per visualizzare il grafico in una finestra Java Swing 
	 * @param urlString Url dell'oggetto chart
	 *  **/

	private static void displayUrlString(final String urlString) throws IOException {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(ImageIO.read(new URL(urlString))));
		label.setSize(800, 800);
		frame.setSize(800, 800);
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

	}

	/** 
	 * Metodo per la generazione del colore Random.
	 * 
	 *  @return Una lista di 5 colori da utilizzare per le curve. Un grafico con più di 5 curve
	 *  diventa difficilmente leggibile
	 *  **/

	private static ArrayList<Color> generateColor() {
		ArrayList<Color> colorList= new ArrayList<Color>();
		colorList.add(Color.BLUE);
		colorList.add(Color.RED);
		colorList.add(Color.GREEN);
		colorList.add(Color.VIOLET);
		colorList.add(Color.ORANGE);


		return colorList;
	}


	/**Metodo per la generazione di Shape random. Le shape, insieme ai colori, permettono di identificare le diverse "line"
	 * appartenenti a grafici diversi;
	 * @return Una lista di 6 shape da utilizzare per la creazione delle curve. Un grafico con più di 5 curve diventa difficilmente 
	 * leggibile
	 * **/

	private static ArrayList<Shape> generateShape(){
		ArrayList<Shape> shapeList= new ArrayList<Shape>();
		shapeList.add(Shape.CIRCLE);
		shapeList.add(Shape.DIAMOND);
		shapeList.add(Shape.SQUARE);
		shapeList.add(Shape.CROSS);
		shapeList.add(Shape.X);

		return shapeList;
	}


	public  void drawAndSaveMDTtoSNRGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path) throws IOException{
    ArrayList<Color> colorList=generateColor();
	ArrayList<Shape> shapeList=generateShape();
	int i=0;
	// Definisco un array di Lines. In questo array inserisco i diversi
	// grafici che voglio visualizzare
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Integer> snr= new ArrayList<Integer>();
	for(int j=inf;j<sup;j++){
		snr.add(j);
	}
	for (String graphName : detection.keySet()) {
		ArrayList<Double> mdt= new ArrayList<Double>();
		for(int h=0;h<detection.get(graphName).size();h++){
			mdt.add(SignalProcessor.computeMDT(detection.get(graphName).get(h)));
		}
		
		Line line=Plots.newLine(Data.newData(mdt), colorList.get(i), graphName);
		//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
		line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
		line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
		lines.add(line);
		i++;
	}

	// Definisco il chart
	LineChart chart = GCharts.newLineChart(lines);
	chart.setSize(665, 450); // Massima dimensione
	chart.setTitle(title, BLACK, 14);
	chart.setGrid(5, 5, 3, 2);

	// Definisco lo stile
	AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

	// Etichetta asse y(% di detection)
	AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 1000000);
	yAxis.setAxisStyle(axisStyle);

	// Etichetta asse x(SNR in DB)
	AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
	xAxis1.setAxisStyle(axisStyle);

	// Etichetta asse x
	AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
	xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

	// Etichetta asse y
	AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("MDT");
	yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

	// Aggiungo al chart
	chart.addXAxisLabels(xAxis1);
	chart.addYAxisLabels(yAxis);
	chart.addXAxisLabels(xAxis3);
	chart.addYAxisLabels(yAxis3);

	// Parametri generali su aspetto
	chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
	LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
	fill.addColorAndOffset(WHITE, 0);
	chart.setAreaFill(fill);

	String imageUrl = chart.toURLString();
	saveImage(imageUrl, path);

}
		
	



	public void drawMDTtoSNRGraph(String title, HashMap<String, ArrayList<Double>> detection, int inf, int sup) throws IOException {
		ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			ArrayList<Double> mdt= new ArrayList<Double>();
			for(int h=0;h<detection.get(graphName).size();h++){
				mdt.add(SignalProcessor.computeMDT(detection.get(graphName).get(h)));
			}
			Line line=Plots.newLine(Data.newData(mdt), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 1000000);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("MDT", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		// Mostro il grafico tramite java swing
		displayUrlString(chart.toURLString());
	}
		
	public void drawMDTtoSNRRatioGraph(String title, HashMap<String, ArrayList<Double>> detection, int inf, int sup,
			String path) throws IOException {
			ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			ArrayList<Double> mdt= new ArrayList<Double>();
			for(int h=0;h<detection.get(graphName).size();h++){
				mdt.add(SignalProcessor.computeMDT(detection.get(graphName).get(h)));
			}
			Line line=Plots.newLine(Data.newData(mdt), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 5);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("MDT Ratio", 50.0);
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		// Mostro il grafico tramite java swing
		displayUrlString(chart.toURLString());
	}
	
	public void drawAndSaveMDTtoSNRRatioGraph(String title, HashMap<String, ArrayList<Double>> detection, int inf,
			int sup, String path) throws IOException {
		ArrayList<Color> colorList=generateColor();
		ArrayList<Shape> shapeList=generateShape();
		int i=0;
		// Definisco un array di Lines. In questo array inserisco i diversi
		// grafici che voglio visualizzare
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Integer> snr= new ArrayList<Integer>();
		for(int j=inf;j<sup;j++){
			snr.add(j);
		}
		for (String graphName : detection.keySet()) {
			ArrayList<Double> mdt= new ArrayList<Double>();
			for(int h=0;h<detection.get(graphName).size();h++){
				mdt.add(SignalProcessor.computeMDT(detection.get(graphName).get(h)));
			}
			
			Line line=Plots.newLine(Data.newData(mdt), colorList.get(i), graphName);
			//Line line = Plots.newLine(Data.newData(detection.get(graphName)), colorList.get(i), graphName);
			line.setLineStyle(LineStyle.newLineStyle(2, 1, 0));
			line.addShapeMarkers(shapeList.get(i), colorList.get(i), 8);
			lines.add(line);
			i++;
		}

		// Definisco il chart
		LineChart chart = GCharts.newLineChart(lines);
		chart.setSize(665, 450); // Massima dimensione
		chart.setTitle(title, BLACK, 14);
		chart.setGrid(5, 5, 3, 2);

		// Definisco lo stile
		AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);

		// Etichetta asse y(% di detection)
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(0, 5);
		yAxis.setAxisStyle(axisStyle);

		// Etichetta asse x(SNR in DB)
		AxisLabels xAxis1 = AxisLabelsFactory.newNumericRangeAxisLabels(inf, sup);
		xAxis1.setAxisStyle(axisStyle);

		// Etichetta asse x
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("SNR (Decibel)", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Etichetta asse y
		AxisLabels yAxis3 = AxisLabelsFactory.newAxisLabels("MDT Ratio");
		yAxis3.setAxisStyle(AxisStyle.newAxisStyle(BLACK, 14, AxisTextAlignment.CENTER));

		// Aggiungo al chart
		chart.addXAxisLabels(xAxis1);
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis3);
		chart.addYAxisLabels(yAxis3);

		// Parametri generali su aspetto
		chart.setBackgroundFill(Fills.newSolidFill(ALICEBLUE));		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		LinearGradientFill fill = Fills.newLinearGradientFill(0, LAVENDER, 100);
		fill.addColorAndOffset(WHITE, 0);
		chart.setAreaFill(fill);

		String imageUrl = chart.toURLString();
		saveImage(imageUrl, path);

	
	}


	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}



}
	


