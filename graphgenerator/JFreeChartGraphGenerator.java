package graphgenerator;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import signalprocessing.SignalProcessor;


public class JFreeChartGraphGenerator extends ApplicationFrame implements GraphGenerator{
	
	private static final long serialVersionUID = 1L;
	static int numberOfGraph;
	public JFreeChartGraphGenerator(String title) {
		super(title);
	}

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

	public void drawSNRtoDetectionGraph(String title, HashMap<String, ArrayList<Double>> detection, int inf, int sup){
		final XYDataset dataset = createSnrDataset(inf,sup,detection);
		final JFreeChart chart = createChart(dataset,super.getTitle(),"SNR","% Detection");
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 500));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

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

	public  void drawAndSaveSNRtoDetectionGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path){
		final XYDataset dataset = createSnrDataset(inf,sup,detection);
		final JFreeChart chart = createChart(dataset,super.getTitle(),"SNR","% Detection");
		final ChartPanel chartPanel = new ChartPanel(chart);
		setContentPane(chartPanel);

		try {


			ChartUtilities.saveChartAsPNG(new File(path), chart, 750, 490);

		} catch (IOException ex) {

			System.out.println(ex.getLocalizedMessage());

		}}


	/**
	 * Metodo per la creazione del grafico % Utenti Malevoli-Detection
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param totalUser Utenti totali coinvolti nella comunicazione
	 * @param totalMaliciousUser Utenti malevoli totali coinvolti nella comunicazione
	 * @throws IOException 
	 **/

	public void drawMaliciousUsersToDetectionGraph(String title, HashMap<String, ArrayList<Double>> detection,
			int totalUser, int totalMaliciousUser) throws IOException {
		final XYDataset dataset = createMSUDataset(detection,totalUser);
		final JFreeChart chart = createChart(dataset,super.getTitle(),"% MSU","% Detection");
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 500));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

	}

	/**
	 * Metodo per la creazione del grafico % Utenti Malevoli-Detection e salvataggio su path specifico
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param totalUser Utenti totali coinvolti nella comunicazione
	 * @param totalMaliciousUser Utenti malevoli totali coinvolti nella comunicazione
	 * @param path Destinazione in cui salvare l'immagine
	 * @throws IOException 
	 **/

	public void drawAndSaveMaliciousUsersToDetectionGraph(String title,
			HashMap<String, ArrayList<Double>> detection, int totalUser, int totalMaliciousUser, String path) throws IOException {
		final XYDataset dataset = createMSUDataset(detection,totalUser);
		final JFreeChart chart = createChart(dataset,super.getTitle(),"% MSU","% Detection");
		final ChartPanel chartPanel = new ChartPanel(chart);

		setContentPane(chartPanel);

		try {
			ChartUtilities.saveChartAsPNG(new File(path), chart, 750, 490);
		} catch (IOException ex) {
			System.out.println(ex.getLocalizedMessage());
		}}

	
	/**
	 * Metodo per la creazione del grafico SNR-MDT
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @throws IOException 
	 **/

	public void drawMDTtoSNRGraph(String title, HashMap<String, ArrayList<Double>> detection, int inf, int sup){
		final XYDataset dataset = createMDTDataset(inf,sup,detection);
		final JFreeChart chart = createChart(dataset,super.getTitle(),"SNR","MTD");
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 500));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

	}
	
	/**
	 * Metodo per la creazione del grafico SNR-Rapporto traMDT
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR su cui è stata effettuata la simulazione
	 * @param sup Estremo superiore di SNR su cui è stata effettuata la simulazione
	 * @throws IOException 
	 **/

	public void drawMDTtoSNRRatioGraph(String title, HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path) throws IOException {
		final XYDataset dataset = createSnrDataset(inf,sup,detection);
		final JFreeChart chart = createChart(dataset,super.getTitle(),"SNR","MTD Ratio");
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(650, 500));
		setContentPane(chartPanel);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

	}
	
	/**
	 * Metodo per la creazione del grafico SNR-MDT e salvataggio su path specifico
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param totalUser Utenti totali coinvolti nella comunicazione
	 * @param totalMaliciousUser Utenti malevoli totali coinvolti nella comunicazione
	 * @param path Destinazione in cui salvare l'immagine
	 * @throws IOException 
	 **/

	public  void drawAndSaveMDTtoSNRGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path){
		final XYDataset dataset = createMDTDataset(inf,sup,detection);
		final JFreeChart chart = createMDTChart(dataset,super.getTitle(),"SNR","MTD");
		final ChartPanel chartPanel = new ChartPanel(chart);
		setContentPane(chartPanel);

		try {


			ChartUtilities.saveChartAsPNG(new File(path), chart, 950, 690);

		} catch (IOException ex) {

			System.out.println(ex.getLocalizedMessage());

		}}
	
	/**
	 * Metodo per la creazione del grafico SNR-Rapporto tra MDT e salvataggio su path specifico
	 * 
	 * @param title Titolo del grafico
	 * @param detection Mappa che ha come chiave il nome della curva da visualizzare e come valore una lista con le percentuali di 
	 * Detection al variare dell'SNR.
	 * @param inf Estremo inferiore di SNR
	 * @param sup Estremo superiore di SNR
	 * @param path Destinazione in cui salvare l'immagine
	 * @throws IOException 
	 **/

	public  void drawAndSaveMDTtoSNRRatioGraph(String title,HashMap<String, ArrayList<Double>> detection, int inf, int sup,String path){
		final XYDataset dataset = createSnrDataset(inf,sup,detection);
		final JFreeChart chart = createMDTChart(dataset,super.getTitle(),"SNR","MTD Ratio");
		final ChartPanel chartPanel = new ChartPanel(chart);
		
		setContentPane(chartPanel);

		try {


			ChartUtilities.saveChartAsPNG(new File(path), chart, 950, 690);

		} catch (IOException ex) {

			System.out.println(ex.getLocalizedMessage());

		}}
	
	
	/** Metodo per la creazione del dataset del grafico MTD-SNR
	 * @param inf Estremo inferiore di snr
	 * @param sup Estremo superiore di SNR
	 * @param detection Mappa con nome del grafico e lista delle detection da rappresentare
	 * @return Un dataset formato da SNR e rispettiva probabilità di detection
	 */

	private  XYDataset createMDTDataset(int inf, int sup,HashMap<String, ArrayList<Double>> detection) {
		numberOfGraph=0;
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (String graphName : detection.keySet()) {
			numberOfGraph++;
			final XYSeries series = new XYSeries(graphName);
			for(int i=0;i<detection.get(graphName).size();i++){
				series.add((double)inf+i, SignalProcessor.computeMDT(detection.get(graphName).get(i)));

			}

			dataset.addSeries(series);
		}


		return dataset;

	}
	
	/** Metodo per la creazione del dataset del grafico Detection-SNR
	 * @param inf Estremo inferiore di snr
	 * @param sup Estremo superiore di SNR
	 * @param detection Mappa con nome del grafico e lista delle detection da rappresentare
	 * @return Un dataset formato da SNR e rispettiva probabilità di detection
	 */

	private  XYDataset createSnrDataset(int inf, int sup,HashMap<String, ArrayList<Double>> detection) {
		numberOfGraph=0;
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (String graphName : detection.keySet()) {
			numberOfGraph++;
			final XYSeries series = new XYSeries(graphName);
			for(int i=0;i<detection.get(graphName).size();i++){
				series.add((double)inf+i, detection.get(graphName).get(i));

			}

			dataset.addSeries(series);
		}


		return dataset;

	}


	/** Metodo per la creazione del dataset del grafico %Utenti malevoli-Detection
	 * @param inf Estremo inferiore di snr
	 * @param sup Estremo superiore di SNR
	 * @param detection Mappa con nome del grafico e lista delle detection da rappresentare
	 * @param totalUser Utenti totali coinvolti nella comunicazione
	 * @return Un dataset formato da % di teunti malevoli e rispettiva probabilità di detection
	 */

	private static XYDataset createMSUDataset(HashMap<String, ArrayList<Double>> detection,int totalUser) {
		numberOfGraph=0;
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (String graphName : detection.keySet()) {
			numberOfGraph++;
			final XYSeries series = new XYSeries(graphName);
			for(int i=0;i<detection.get(graphName).size();i++){
				System.out.println(((double)i/(double)totalUser)*100);
				System.out.println(totalUser);
				series.add(((double)i/(double)totalUser)*100, detection.get(graphName).get(i));

			}

			dataset.addSeries(series);
		}


		return dataset;

	}

	/**
	 * Metodo per la creazione del chart 
	 * @param dataset  Dataset contenente i dati da rappresentare
	 * @param title titolo del grafico
	 * @xAxis label dell'asse x
	 * @yAxis label asse y 
	 * @return Un chart.
	 */

	private static JFreeChart createChart(final XYDataset dataset,String title,String xAxis,String yAxis) {
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,      // chart title
				xAxis,                      // x axis label
				yAxis,                      // y axis label
				dataset,                  // data
				PlotOrientation.VERTICAL,
				true,                     // include legend
				true,                     // tooltips
				false                     // urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer =  new XYLineAndShapeRenderer(true, true);
		plot.setRenderer(renderer);
	
		ArrayList<Color> colorList=generateColor();

		for(int i=0;i<numberOfGraph;i++){
			plot.getRenderer().setSeriesPaint(i, colorList.get(i));	
		}
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		XYItemLabelGenerator generator =
				new StandardXYItemLabelGenerator(StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,format, format);
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		return chart;

	}


	/**
	 * Metodo per la creazione del chart 
	 * @param dataset  Dataset contenente i dati da rappresentare
	 * @param title titolo del grafico
	 * @xAxis label dell'asse x
	 * @yAxis label asse y 
	 * @return Un chart.
	 */

	private static JFreeChart createMDTChart(final XYDataset dataset,String title,String xAxis,String yAxis) {
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,      // chart title
				xAxis,                      // x axis label
				yAxis,                      // y axis label
				dataset,                  // data
				PlotOrientation.VERTICAL,
				true,                     // include legend
				true,                     // tooltips
				false                     // urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer renderer =  new XYLineAndShapeRenderer(true, true);
		plot.setRenderer(renderer);
		
		ValueAxis yaxis = plot.getRangeAxis();
		yaxis.setRange(-50000.00, 1000001.00);
		ArrayList<Color> colorList=generateColor();

		for(int i=0;i<numberOfGraph;i++){
			plot.getRenderer().setSeriesPaint(i, colorList.get(i));	
		}
		renderer.setBaseShapesVisible(true);
		renderer.setBaseShapesFilled(true);
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		XYItemLabelGenerator generator =
				new StandardXYItemLabelGenerator(StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,format, format);
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		return chart;

	}
	
	/** 
	 * Metodo per la generazione del colore Random.	 * 
	 *  @return Una lista di 5 colori da utilizzare per le curve. Un grafico con più di 5 curve
	 *  diventa difficilmente leggibile
	 *  **/

	private static ArrayList<Color> generateColor() {
		ArrayList<Color> colorList= new ArrayList<Color>();
		colorList.add(Color.BLUE);
		colorList.add(Color.RED);
		colorList.add(Color.GREEN);
		colorList.add(Color.ORANGE);
		return colorList;
	}



	

}
	


