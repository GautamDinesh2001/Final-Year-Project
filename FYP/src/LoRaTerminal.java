
import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gnu.io.CommPort;

public class LoRaTerminal {
	private InputStream serialPortInputStream;
	private OutputStream serialPortOutputStream;
	private CommPort serialPort;
	private StringBuilder content;
	private final byte idle = 0;
	private final byte exit = 1;
	private final byte end = 2;
	private final byte raw = 3;
	private final byte measureAlpha = 4;
	private final byte linealityTest = 5;
	private final byte locateTarget = 6;
	private final byte measureAlphaNlocateTarget = 7;
	private final byte measureAlphaNlocateTargetMirror = 8;
	private final byte st2n = 9;
	private final byte record = 10;
	private final byte discover = 11;
	Calculate cal;
	SDcal sdcal;
	Gui gui;
	int tempT = 10;
	boolean recordcheck = false;
	public static String input;
	public static boolean newInput;

	public LoRaTerminal(List<Object> inOutputStream) {
		serialPortInputStream = (InputStream) inOutputStream.get(0);
		serialPortOutputStream = (OutputStream) inOutputStream.get(1);
		serialPort = (CommPort) inOutputStream.get(2);
	}

	public boolean start(Scanner scanner) throws IOException {
		short numberOfSample = 100;
		List<Integer> availableNode = new ArrayList<Integer>();
		List<Double> RSSI = new ArrayList<Double>();
		double tempDistance = 0.0;
		List<Double> distance = new ArrayList<Double>();
		List<List<Double>> RSSIList = new ArrayList<List<Double>>();
		List<List<Double>> AlphaRSSIList = new ArrayList<List<Double>>();
		List<List<String>> tempListForCAL = new ArrayList<List<String>>();
		double alpha = 0;
		double RSSI1m = 0;
		List<Double> baseX = new ArrayList<Double>();
		List<Double> baseY = new ArrayList<Double>();
		double MasterX = 0;
		double MasterY = 0;
		double TargetX = 0;
		double TargetY = 0;
		int MaxX = 0;
		int MaxY = 0;
		int availableN = 0;
		List<List<Integer>> list = new ArrayList<>();
		List<List<Integer>> list2= new ArrayList<>();
		List<List<Integer>> tollslist=new ArrayList<>();
		 List<Integer> temptollslist=new ArrayList<>();
		List<String> receivedRSSIList = new ArrayList<>();
		List<String> receivedRecordR = new ArrayList<>();
		List<List<String>> ComListR = new ArrayList<>();
		List<List<String>> ComList = new ArrayList<>();
		List<String> templistforSD = new ArrayList<>();
		int initNum = 0;

		boolean mirrorFlag = false;
		boolean toExit = false;
		File textLog;
		SimpleDateFormat fileNameExtension = new SimpleDateFormat("yyyyMMMdd_HHmmss");
		textLog = new File("C:\\Users\\gauat\\Desktop\\Final year Project\\Final year Project",
				"log" + fileNameExtension.format(new Date()) + ".txt");
		content = new StringBuilder();

		input = new String();
		newInput = false;
		List<String> inputToken = new ArrayList<String>();
		byte currentOperation = idle;
		byte currentOperationStage = 0;
		byte COS = 0;
		byte COS_R = 0;
		byte COS_D = 0;
		Thread inputReader = new Thread(new SerialReader(scanner));
		inputReader.start();

		StringBuilder SerialPortInputTransmitionBuffer = new StringBuilder();
		List<String> SerialPortInputTransmition = new ArrayList<String>();

		byte[] buffer = new byte[1024];
		int len = -1;
		JFrame f = new JFrame();
		f.setTitle(serialPort.getName() + " Monitor");

		JPanel jp = new JPanel();

		JTextArea panel = new JTextArea(20, 40);
		panel.setEditable(false);
		DefaultCaret caret = (DefaultCaret) panel.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scollPane = new JScrollPane(panel);

		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		jp.add(scollPane, c);

		f.add(jp);
		f.pack();
		f.setVisible(true);

		terminalLoop: while (true) {
			if ((len = serialPortInputStream.read(buffer)) > -1) {
				String inString = new String(buffer, 0, len);
				if (!inString.isEmpty()) {
					String[] inStrings = inString.split("\n");
					for (int i = 0; i < inStrings.length; i++) {
						if (i > 0) {
							String fullSingleInputTransmition = SerialPortInputTransmitionBuffer.toString() + "\n";
							SerialPortInputTransmition.add(fullSingleInputTransmition);
							content.append(">>" + fullSingleInputTransmition);
							panel.append(fullSingleInputTransmition);
							SerialPortInputTransmitionBuffer = new StringBuilder("");
						}
						SerialPortInputTransmitionBuffer.append(inStrings[i]);
					}
					if (inString.endsWith("\n")) {
						String fullSingleInputTransmition = SerialPortInputTransmitionBuffer.toString() + "\n";
						SerialPortInputTransmition.add(fullSingleInputTransmition);
						content.append(">>" + fullSingleInputTransmition);
						panel.append(fullSingleInputTransmition);
						SerialPortInputTransmitionBuffer = new StringBuilder("");
					}
				}

			}
			if (newInput) {
				content.append("<<" + input + '\n');
				inputToken = Arrays.asList(input.split(" "));
				input = new String("");
				newInput = false;

				StringBuilder userInput = new StringBuilder();
				userInput.append("Received:");
				Iterator<String> commandIterator = inputToken.iterator();
				while (commandIterator.hasNext()) {
					userInput.append(" " + commandIterator.next());
				}
				userInput.append('\n');
				systemOut(userInput.toString());
			}
			if (!inputToken.isEmpty()) {
				if (inputToken.get(0).equalsIgnoreCase("break")) {
					currentOperation = idle;
					currentOperationStage = 0;
					inputToken = new ArrayList<String>();
				}
			}
			switch (currentOperation) {
			case (idle): {
				SerialPortInputTransmition = new ArrayList<String>();
				if (!inputToken.isEmpty()) {
					if (inputToken.get(0).equalsIgnoreCase("exit")) {
						currentOperation = exit;
					} else if (inputToken.get(0).equalsIgnoreCase("end")) {
						currentOperation = end;
					} else if (inputToken.get(0).equalsIgnoreCase("raw") && inputToken.size() > 1) {
						 currentOperation = raw;
					} else if (inputToken.get(0).equalsIgnoreCase("measureAlpha") && inputToken.size() > 1) {
						 currentOperation = measureAlpha;
					} else if (inputToken.get(0).equalsIgnoreCase("linealityTest")) {
						 currentOperation = linealityTest;
					} else if (inputToken.get(0).equalsIgnoreCase("locateTarget") && inputToken.size() > 1) {
						 currentOperation = locateTarget;
					} else if (inputToken.get(0).equalsIgnoreCase("locateTargetNAlpha") && inputToken.size() > 1) {
						 currentOperation = measureAlphaNlocateTarget;
					} else if (inputToken.get(0).equalsIgnoreCase("measureAlphaNTargetMi") && inputToken.size() > 1) {
						 currentOperation = measureAlphaNlocateTargetMirror;
					} else if ((inputToken.get(0).equalsIgnoreCase("st2n"))) {
						currentOperation = st2n;
						inputToken = new ArrayList<String>();
					} else if (inputToken.get(0).equalsIgnoreCase("record")) {
						// if(availableNode.size()!=0)
						// {
						currentOperation = record;
						COS_R=5;
						availableN = 0;
						inputToken = new ArrayList<String>();
						System.out.println("record");
						// }
						// else
						{
							// systemOut("no availabel node can record\n");
							// currentOperation=idle;
						}
					} else if (inputToken.get(0).equalsIgnoreCase("discover")) {
						currentOperation = discover;
						COS_D = 0;
					} else {
						systemOut("Unknown command/Incorrect use of command\n");
						inputToken = new ArrayList<String>();
					}
				}
				break;
			}
			case (end): {
				toExit = false;
				break terminalLoop;
			}
			case (exit): {
				toExit = true;
				break terminalLoop;
			}
			case (raw): {
				Iterator<String> commands = inputToken.iterator();
				commands.next();
				StringBuilder rawCommand = new StringBuilder(commands.next().toLowerCase());
				while (commands.hasNext()) {
					rawCommand.append(" " + commands.next().toLowerCase());
				}
				sendToSerial(rawCommand.toString() + '\n');
				currentOperation = idle;
				inputToken = new ArrayList<String>();
				break;
			}
			case (measureAlpha): {
				currentOperationStage = 0;
				switch (currentOperationStage) {
				case (0): {
					SerialPortInputTransmition = new ArrayList<String>();
					sendToSerial("timestampadjustment\n");
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sendToSerial("discover 2\n");
					numberOfSample = (short) Integer.parseInt(inputToken.get(1));
					systemOut("Measure Alpha with " + numberOfSample + " sample\n");
					currentOperationStage = 1;
					break;
				}
				case (1): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("Total number of ")) {
							if (Integer.parseInt(line.substring(16, 17)) > 1) {
								systemOut("Input ok when ready to measure 1m RSSI\n");
								currentOperationStage = 2;
							} else {
								systemOut("Not enough available node! Return to idle\n");
								currentOperationStage = 0;
								currentOperation = idle;
							}
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (2): {
					if (!inputToken.isEmpty()) {
						if (inputToken.get(0).equalsIgnoreCase("ok"))
							;
						{
							currentOperationStage = 3;
							RSSIList = new ArrayList<List<Double>>();
							RSSI = new ArrayList<Double>();
							inputToken = new ArrayList<String>();
							sendToSerial("targettobasetransmitionn " + numberOfSample + "\n");
						}
					}
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (3): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("PKR:")) {
							RSSI.add(
									Double.parseDouble(line.substring(line.indexOf("PKR:") + 4, line.indexOf(" SNR"))));
						}
						if (line.contains("End")) {
							systemOut("Input ok when ready to measure 10m RSSI\n");
							RSSIList.add(RSSI);
							RSSI = new ArrayList<Double>();
							currentOperationStage = 4;
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (4): {
					if (!inputToken.isEmpty()) {
						if (inputToken.get(0).equalsIgnoreCase("ok"))
							;
						{
							currentOperationStage = 5;
							inputToken = new ArrayList<String>();
							sendToSerial("targettobasetransmitionn " + numberOfSample + "\n");
						}
					}
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (5): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("PKR:")) {
							RSSI.add(
									Double.parseDouble(line.substring(line.indexOf("PKR:") + 4, line.indexOf(" SNR"))));
						}
						if (line.contains("End")) {
							RSSI1m = PequodsMaths.getMean(RSSIList.get(0));
							double RSSI1Mean = PequodsMaths.getMean(RSSI);
							alpha = PequodsMaths.calculateAlpha(RSSI1m, RSSI1Mean);
							systemOut("RSSI 1m mean: " + RSSI1m + '\n');
							systemOut("RSSI 10m mean: " + RSSI1Mean + '\n');
							systemOut("Alpha: " + alpha + '\n');
							currentOperationStage = 0;
							currentOperation = idle;
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				default: {
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				}
				break;
			}
			case (linealityTest): {
				switch (currentOperationStage) {
				case (0): {
					SerialPortInputTransmition = new ArrayList<String>();
					sendToSerial("timestampadjustment\n");
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sendToSerial("discover\n");
					systemOut("lineality Test\n");
					currentOperationStage = 1;
					break;
				}
				case (1): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("Total number of ")) {
							if (Integer.parseInt(line.substring(16, 17)) > 1) {
								systemOut("Test starts now\n");
								RSSI = new ArrayList<Double>();
								distance = new ArrayList<Double>();
								currentOperationStage = 2;
							} else {
								systemOut("Not enough available node! Return to idle\n");
								currentOperationStage = 0;
								currentOperation = idle;
							}
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (2): {
					systemOut("Input distance(m) if ready to measure RSSI, Input check if it is enough\n");
					currentOperationStage = 3;
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (3): {
					if (!inputToken.isEmpty()) {
						if (inputToken.get(0).equalsIgnoreCase("check")) {
							currentOperationStage = 5;
						} else {
							tempDistance = Double.parseDouble(inputToken.get(0));
							currentOperationStage = 4;
							sendToSerial("targettobasetransmitionn " + 1 + "\n");
							inputToken = new ArrayList<String>();
						}
					}
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (4): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("PKR:")) {
							RSSI.add(
									Double.parseDouble(line.substring(line.indexOf("PKR:") + 4, line.indexOf(" SNR"))));
							distance.add(new Double(tempDistance));
						}
						if (line.contains("End")) {
							currentOperationStage = 2;
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (5): {
					systemOut("Start calculation\n");
					File outputxlsxFile = new File(
							"C:\\Users\\gauat\\Desktop\\Final year Project\\Final year Project\\logbook"
									+ fileNameExtension.format(new Date()) + "Book.xlsx");
					XSSFWorkbook outputworkbook = new XSSFWorkbook();
					POIXMLProperties xmlProps = outputworkbook.getProperties();
					POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
					coreProps.setCreator("Pequod");
					XSSFSheet rawDataSheet = outputworkbook.createSheet("RawData");
					int rawDataSheetCurrentRow = 0;

					XSSFRow tagRow = rawDataSheet.createRow(0);
					tagRow.createCell(0, CellType.STRING).setCellValue("Distance");
					tagRow.createCell(1, CellType.STRING).setCellValue("RSSI");
					rawDataSheetCurrentRow++;

					for (int index = 0; index < distance.size(); index++)// cal
					{
						XSSFRow recordRow = rawDataSheet.createRow(rawDataSheetCurrentRow);
						recordRow.createCell(0, CellType.NUMERIC).setCellValue(distance.get(index));
						recordRow.createCell(1, CellType.NUMERIC).setCellValue(RSSI.get(index));
						systemOut("Distance: " + distance.get(index) + ", mean RSSI: " + RSSI.get(index) + "\n");
						rawDataSheetCurrentRow++;
					}
					double[] result = PequodsMaths.LinealityTest(distance, RSSI);
					systemOut("Slope: " + result[0] + '\n');
					systemOut("R square: " + result[1] + '\n');
					FileOutputStream writeFile = new FileOutputStream(outputxlsxFile);
					outputworkbook.write(writeFile);
					outputworkbook.close();
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					currentOperationStage = 0;
					currentOperation = idle;
					break;
				}
				default: {
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				}
				break;
			}
			case (locateTarget): {
				switch (currentOperationStage) {
				case (0): {
					if (alpha == 0) {
						systemOut("Can't locate target without a measured Alpha\n");
						currentOperationStage = 0;
						currentOperation = idle;
						inputToken = new ArrayList<String>();
						SerialPortInputTransmition = new ArrayList<String>();
					} else {
						sendToSerial("discover\n");
						numberOfSample = (short) Integer.parseInt(inputToken.get(1));
						systemOut("locate target with " + numberOfSample + " sample\n");
						availableNode = new ArrayList<Integer>();
						baseX = new ArrayList<Double>();
						baseY = new ArrayList<Double>();
						currentOperationStage = 1;
					}
					break;
				}
				case (1): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("Available Nodes: ")) {
							String[] nodeAddress = line.substring(line.indexOf(", ") + 2, line.length() - 2)
									.split(", ");
							for (int index = 0; index < nodeAddress.length; index++) {
								availableNode.add(Integer.parseInt(nodeAddress[index]));
							}
						}
						if (line.contains("Total number of ")) {
							if (Integer.parseInt(line.substring(16, 17)) > 3) {
								RSSIList = new ArrayList<List<Double>>(availableNode.size());
								for (int index = 0; index < availableNode.size(); index++) {
									RSSIList.add(new ArrayList<Double>());
								}
								currentOperationStage = 2;
							} else {
								systemOut("Not enough available node! Return to idle\n");
								currentOperationStage = 0;
								currentOperation = idle;
							}
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (2): {
					systemOut("Input the x, y coordinate for BaseStation address: " + availableNode.get(baseX.size())
							+ "\n");
					SerialPortInputTransmition = new ArrayList<String>();
					inputToken = new ArrayList<String>();
					currentOperationStage = 3;
					break;
				}
				case (3): {
					if (!inputToken.isEmpty()) {
						baseX.add(new Double(inputToken.get(0)));
						baseY.add(new Double(inputToken.get(1)));
						if (baseX.size() == availableNode.size()) {
							currentOperationStage = 4;
							SerialPortInputTransmition = new ArrayList<String>();
							sendToSerial("targettobasetransmitionn " + numberOfSample + "\n");
						} else {
							currentOperationStage = 2;
						}
					}
					inputToken = new ArrayList<String>();
					break;
				}
				case (4): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("T2B PKR:")) {
							int nodeAddress = Integer
									.parseInt(line.substring(line.indexOf("addr:") + 5, line.indexOf(" T2B")));
							RSSIList.get(availableNode.indexOf(new Integer(nodeAddress)))
									.add(new Double(line.substring(line.indexOf("PKR:") + 4, line.indexOf(" SNR"))));
						}
						if (line.contains("End")) {
							systemOut("Start calculation\n");
							List<Double> RSSImean = new ArrayList<Double>();
							for (int index = 0; index < availableNode.size(); index++) {
								double mean = PequodsMaths.getMean(RSSIList.get(index));
								RSSImean.add(mean);
								systemOut("node address:" + availableNode.get(index) + " x:" + baseX.get(index) + " y:"
										+ baseY.get(index) + " RSSI:" + mean + "\n");
							}
							systemOut("1m RSSI: " + RSSI1m + "\n");
							systemOut("Alpha: " + alpha + "\n");
							double[] result = PequodsMaths.LLSForLocalization(RSSImean, baseX, baseY, RSSI1m, alpha);
							systemOut("x: " + result[0] + "\n");
							systemOut("y: " + result[1] + "\n");
							systemOut("x^2+y^2: " + result[2] + "\n");
							systemOut("R square: " + result[3] + "\n");
							currentOperationStage = 0;
							currentOperation = idle;
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				default: {
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				}
			}
			case (st2n): {
			    List<float[]> lines;
			    switch (COS) {
			        case (0): {
			            COS = 2;
			            baseX = new ArrayList<Double>();
			            baseY = new ArrayList<Double>();
			            list = new ArrayList<>();
			            lines = new ArrayList<>();
			            break;
			        }
			        case (1): {
			            Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
			            while (SerialPortInputTransmitions.hasNext()) {
			                String line = SerialPortInputTransmitions.next();
			                if (line.contains("Available Nodes: ")) {
			                    String temp = line.substring(17);
			                    String[] temp2 = temp.split(",");
			                    for (int index = 0; index < temp2.length - 1; index++) {
			                        String nodeAddress = temp2[index].trim();
			                        if (nodeAddress != "" || nodeAddress != " " || nodeAddress != null) {
			                            availableNode.add(Integer.parseInt(nodeAddress));
			                        }
			                    }
			                }
			                if (line.contains("Total number of ")) {
			                    if (availableNode.size() >= 3) {
			                        RSSIList = new ArrayList<List<Double>>(availableNode.size());
			                        AlphaRSSIList = new ArrayList<List<Double>>();
			                        for (int index = 0; index < availableNode.size(); index++) {
			                            RSSIList.add(new ArrayList<Double>());
			                            AlphaRSSIList.add(new ArrayList<Double>());
			                        }
			                        COS = 2;
			                    } else {
			                        systemOut("Not enough available node! Return to idle\n");
			                        COS = 0;
			                        currentOperation = idle;
			                    }
			                }
			            }
			            inputToken = new ArrayList<String>();
			            SerialPortInputTransmition = new ArrayList<String>();
			            break;
			        }
			        case (2): {
			            StringBuilder stringOut = new StringBuilder();
			            systemOut("Input the Max X an Y for the map\n");
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            COS = 3;
			            break;
			        }
			        case (3): {
			            if (!inputToken.isEmpty()) {
			                if (inputToken.size() == 2) {
			                    MaxX = new Integer(inputToken.get(0));
			                    MaxY = new Integer(inputToken.get(1));
			                    COS = 4;
			                    SerialPortInputTransmition = new ArrayList<String>();
			                } else {
			                    systemOut("Error Input, check and enter again\n");
			                    COS = 2;
			                }
			            }
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            break;
			        }
			        case (4): {
			            StringBuilder stringOut = new StringBuilder();
			            for (int dummy = 0; dummy < availableNode.size(); dummy++) {
			                stringOut.append(availableNode.get(dummy).toString() + ", ");
			            }
			            systemOut("Input the x, y coordinate for BaseStation address: " + stringOut + "\n");
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            COS = 5;
			            break;
			        }
			        case (5): {
			            if (!inputToken.isEmpty()) {
			                if (inputToken.size() == ((availableNode.size()) * 2)) {
			                    for (int i = 0; i < inputToken.size(); i = i + 2) {
			                        List<Integer> templist = new ArrayList<>();
			                        int x = new Integer(inputToken.get(i));
			                        int y = new Integer(inputToken.get(i + 1));
			                        templist.add(x * 100);
			                        templist.add(y * 100);
			                        list.add(templist);
			                    }
			                    COS = 6;
			                } else {
			                    systemOut("Error Input, check and enter again\n");
			                    COS = 4;
			                }
			            }
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            break;
			        }
			        case (6): {
			            List<List<Integer>> list2temp = new ArrayList<>();
			            for (int i = 0; i < availableNode.size(); i++) {
			                List<Integer> templistForUpdata = new ArrayList<>();
			                List<Integer> xy = list.get(i);
			                templistForUpdata.add(availableNode.get(i));
			                templistForUpdata.add(xy.get(0));
			                templistForUpdata.add(xy.get(1));
			                list2temp.add(templistForUpdata);
			            }
			            cal = new Calculate(availableNode.size(), MaxX * 100, MaxY * 100, list, list2temp);
			            list2 = list2temp;
			            gui = new Gui(cal);
			            float[] tempresult = {0, 0};
			            gui.draw(tempresult);
			            COS = 7;
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            break;
			        }
			        case (7): {
			            sendToSerial("st2ns\n");
			            COS = 8;
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            break;
			        }
			        case (8): {
			            Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
			            while (SerialPortInputTransmitions.hasNext()) {
			                String line = SerialPortInputTransmitions.next();
			                if (line.contains("Base_addr:")) {
			                    String[] temp1 = line.split(" ");
			                    receivedRSSIList = new ArrayList<>();
			                    receivedRSSIList.add(temp1[2].substring(12).trim());
			                    receivedRSSIList.add(temp1[0].substring(10).trim());
			                    receivedRSSIList.add(temp1[1].substring(8).trim());
			                    ComList.add(receivedRSSIList);
			                    temp1 = null;
			                }
			                if (line.contains("timeout")) {
			                    COS = 9;
			                    systemOut("END^^\n");
			                    break;
			                }
			                if (line.contains("END^^")) {
			                    COS = 9;
			                    systemOut("END^^\n");
			                    break;
			                }
			            }
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            break;
			        }
			        case (9): {
			            // print ComList
			            systemOut("stage 9\n");
			            LLS lls= new LLS();
			            System.out.println("result for hand input");
			            for(int i=0; i<ComList.size();i++) 
			            {
			                System.out.print(String.format("%s_%s_%s,", ComList.get(i).get(0).toString(),ComList.get(i)
			                     .get(1).toString(),ComList.get(i).get(2).toString()));
			            }
			            
			            // Compare Rssi and Alpha/****************************************************************
			            Compare comp= new Compare();
			            System.out.println("Before calling compRSSI()");
			            try {
			                comp.compRSSI(tempListForCAL, ComList, list2);
			            } catch (Exception e) {
			                e.printStackTrace();
			                System.err.println("An error occurred while comparing RSSI values: " + e.getMessage());
			            }

			            System.out.println("After calling compRSSI()");

			            //****************************************************************
			            List<List<String>> list2LLS=comp.getlistToLLS();  //get the updated list 
			                                                               // {0: T  1: B  2: RssiD 3: Alpha 4: Tx 5:Ty 6: Bx 7: By 8:distance} element in list2LLS
			            List<List<String>> list2LLSinMax4=comp.getMaxFourRssiDiff();
			            
			            cal.calequation2(list2LLSinMax4);
			            float[][] p4MatrixA=cal.getMatrixA();
			            float[]  p4Matrixb=cal.getMatrixb();
			            if(list2LLSinMax4.size()>2)
			            {
			                try {
			                    lls.LLSc(p4MatrixA, p4Matrixb);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			                float[] resultInMax4=lls.getLLSresult();
			                System.out.println(String.format("x %s y %s z %s", resultInMax4[0],resultInMax4[1],resultInMax4[2]));
			            }
			            
			            System.out.println("list2LLSinMax4 size:"+list2LLSinMax4.size());
			            for(int i=0;i<list2LLSinMax4.size();i++)
			            {
			                System.out.println(String.format("line 894 0:%s 1:%s 2:%s 3:%s",list2LLSinMax4.get(i).get(0),list2LLSinMax4.get(i).get(1),list2LLSinMax4.get(i).get(2),list2LLSinMax4.get(i).get(3)));
			            }
			            
			            if(list2LLS.size()<=1)
			            {
			                System.out.println("case 1 of list2LLS");
			            }
			            else if (list2LLS.size()>=2)
			            {
			                for(int i=0;i<list2LLS.size();i++)
			                {
			                    System.out.println(String.format("T: %s B: %s", list2LLS.get(i).get(0), list2LLS.get(i).get(1)));
			                }
			                tollslist=new ArrayList<>();
			                temptollslist=new ArrayList<>();
			                for(int i=0;i<list2LLS.size();i++)
			                {
			                    for (int j =0; j<list2.size();j++)
			                    {
			                        if(list2.get(j).get(0)==Integer.parseInt(list2LLS.get(i).get(0)))
			                        {
			                            temptollslist.add(list2.get(j).get(0));
			                            temptollslist.add(list2.get(j).get(1));
			                            temptollslist.add(list2.get(j).get(2));
			                        }
			                        if(list2.get(j).get(0)==Integer.parseInt(list2LLS.get(i).get(1)))
			                        {
			                            temptollslist.add(list2.get(j).get(0));
			                            temptollslist.add(list2.get(j).get(1));
			                            temptollslist.add(list2.get(j).get(2));

			                        }
			                    }
			                    tollslist.add(temptollslist);
			                    temptollslist=new ArrayList<>();
			                }
			            }
			            System.out.println("Size of tollslist: " + tollslist.size());

			            for (int i = 0; i < tollslist.size(); i++) {
			                System.out.println(String.format("T: %s, tX: %s, tY: %s, B: %s, bX: %s, bY: %s",
			                        tollslist.get(i).get(0), tollslist.get(i).get(1), tollslist.get(i).get(2),
			                        tollslist.get(i).get(3), tollslist.get(i).get(4), tollslist.get(i).get(5)));
			            }
			            System.out.println("Size of tollslist: " + tollslist.size());
			            for (int i=0; i<tollslist.size();i++)
			            {
			                int t=tollslist.get(i).get(0);
			                int b=tollslist.get(i).get(3);
			                for(int j=i+1;j<tollslist.size();j++)
			                {
			                    int t2=tollslist.get(j).get(0);
			                    int b2=tollslist.get(j).get(3);
			                    if(t==t2 && b==b2)
			                    {
			                        tollslist.remove(j);
			                    }
			                }
			            }
			            
			            System.out.println("deed size:"+tollslist.size());
			            for(int i=0; i<tollslist.size();i++)
			            {
			                System.out.println(String.format("T: %s, tX: %s, tY: %s , B: %s, bX:%s, bY:%s", tollslist.get(i).get(0),
			                        tollslist.get(i).get(1),tollslist.get(i).get(2),tollslist.get(i).get(3),tollslist.get(i).get(4),tollslist.get(i).get(5)));
			            }
					 
					 
					 
					 
					 
					 //get equation
			            cal.calequation2(list2LLSinMax4);
			            float[][] pMatrixA=cal.getMatrixA();
			            float[]  pMatrixb=cal.getMatrixb();
			            List<float[]> lines2=cal.getLines2();
			            System.out.println(String.format("A:%s, b:%s", pMatrixA.length,pMatrixb.length));
			            float[] resultToGuiInCir= {0,0};
			            
			            
			            try {
			                resultToGuiInCir = lls.LLSc(pMatrixA,pMatrixb);
			            } catch (Exception e) {
			                e.printStackTrace();
			            }
			            
			            if(pMatrixA.length<=1)
			            {
			                System.out.println("no result update");
			            }
			            else if(pMatrixA.length==2)
			             {
			                System.out.println("%%%%% A[==2]");
			                cal.calequation2(list2LLSinMax4);
			                 List<float[]> tempLines=cal.getLines2();
			                 resultToGuiInCir=cal.calIntersectionPoint(tempLines);
			                 float resultX=Math.abs(resultToGuiInCir[0]);
			                 float resultY=Math.abs(resultToGuiInCir[1]);
			                 resultToGuiInCir[0]=resultX;
			                 resultToGuiInCir[1]=resultY;
			                 System.out.println(String.format("%s %s", resultToGuiInCir[0], resultToGuiInCir[1]));
			                 gui.closeJframe();
			                 gui.draw(resultToGuiInCir);
			             }
			             else if(pMatrixA.length>=3)
			             {
			             
			                System.out.println("!!!!!!!!!!!!! A[>=3]");
			                 try {
			                    resultToGuiInCir = lls.LLSc(pMatrixA,pMatrixb);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			                 float resultX=Math.abs(resultToGuiInCir[0]);
			                 float resultY=Math.abs(resultToGuiInCir[1]);
			                 resultToGuiInCir[0]=resultX;
			                 resultToGuiInCir[1]=resultY;
			                    System.out.println(String.format("display result x:%s y:%s z:%s", resultX,resultY,resultToGuiInCir[2]));
			                    gui.closeJframe();
			                    gui.draw(resultToGuiInCir);
			            }
			            
			            SerialPortInputTransmition = new ArrayList<String>();
			            inputToken = new ArrayList<String>();
			            System.out.println("Do it again?");
			            COS = 10;
			            break;
			        }
			        case (10): {
			            
			            if (!inputToken.isEmpty())
			            {
			                if(inputToken.get(0).equals("y"))
			                {
			                    System.out.println("case 7 input");
			                    ComList=new ArrayList<>();
			                    COS=7;
			                }
			                else if (inputToken.get(0).equals("n"))
			                {
			                    System.out.println("idle");
			                    currentOperation=idle;
			                    COS=99;
			                }
			                else
			                {
			                    System.out.println("error input");
			                    COS=10;
			                }
			            }
			            inputToken = new ArrayList<String>();
			            SerialPortInputTransmition = new ArrayList<String>();
			            break;
			        }
			        case(11):
			        {
			            
			        }
			        default: {
			            inputToken = new ArrayList<String>();
			            SerialPortInputTransmition = new ArrayList<String>();
			            currentOperation = idle;
			            break;
			             }
			            }
			            break;
			        }





			case (record): {
				
				// availableNode.get(availableN);
				switch (COS_R) 
				{
				case (0): { // systemOut("case0 tmepT:"+tempT+"N:"+availableNode.get(availableN)+"\n");

					if (availableN < availableNode.size()) {
						//System.out.println("availableN:"+ availableNode.size());
						if (tempT != availableNode.get(availableN)) {
							sendToSerial(String.format("recordpingmn %s %s %s", tempT, availableNode.get(availableN), 5)
									+ "\n");
							COS_R = 1;
						} else {
							availableN++;
							COS_R = 0;
						}
					} else {
						systemOut("!!!complete record all node RSSI\\n");
						// availableN=99;
						recordcheck = true;
						COS_R = 3;
					}
					SerialPortInputTransmition = new ArrayList<String>();
					inputToken = new ArrayList<String>();
					break;
				}
				case (1): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("Base_addr:")) {
							// systemOut("case 1\n");
							
							String[] temp1 = line.split(" ");
							receivedRecordR = new ArrayList<>();
							// systemOut("temp1 "+": "+temp1[0]+" "+temp1[1]+" "+temp1[2]+"\n");
							// systemOut(temp1[0].substring(10).trim());
							// systemOut(temp1[1].substring(9).trim());
							// systemOut(temp1[2].substring(12).trim());
							receivedRecordR.add(temp1[2].substring(12).trim());// Target Address
							receivedRecordR.add(temp1[0].substring(10).trim());// Base Address;
							receivedRecordR.add(temp1[1].substring(9).trim()); // RSSI value received by Base Addr from
																				// Target Addr
							// System.out.println("T:"+receivedRSSIList.get(0)+"
							// B:"+receivedRSSIList.get(1)+" R:"+receivedRSSIList.get(2));
							ComListR.add(receivedRecordR);
							// temp1=null;
						} else if (line.contains("timeout recordping")) {
							templistforSD = new ArrayList<>();
							sdcal = new SDcal(ComListR);
							sdcal.calSD();
							templistforSD.add(String.valueOf(tempT));
							templistforSD.add(String.valueOf(availableNode.get(availableN)));
							// systemOut("only:" +sdcal.getSD());
							//double tempsd = sdcal.getSD();
							//double tempmean = sdcal.getMean();
							//systemOut("Dou: " + sdcal.SD + " mean: " + tempmean + "\n");
							templistforSD.add(String.valueOf(sdcal.getSD()));
							templistforSD.add(String.valueOf(sdcal.getMean()));
							systemOut(String.format("T: %s B: %s SD: %s M: %s \n", templistforSD.get(0),
									templistforSD.get(1), templistforSD.get(2).toString(),
									templistforSD.get(3).toString()));
							ComListR = new ArrayList<>();
							COS_R = 2;
						} else if (line.contains("timeout")) {
							templistforSD = new ArrayList<>();
							sdcal = new SDcal(ComListR);
							sdcal.calSD();
							templistforSD.add(String.valueOf(tempT));
							templistforSD.add(String.valueOf(availableNode.get(availableN)));
							// systemOut("only:" +sdcal.getSD());
							//double tempsd = sdcal.getSD();
							//double tempmean = sdcal.getMean();
							//systemOut("Dou: " + sdcal.SD + " mean: " + tempmean + "\n");
							templistforSD.add(String.valueOf(sdcal.getSD()));
							templistforSD.add(String.valueOf(sdcal.getMean()));
							systemOut(String.format("T: %s B: %s SD: %s M: %s \n", templistforSD.get(0),
									templistforSD.get(1), templistforSD.get(2).toString(),
									templistforSD.get(3).toString()));
							ComListR = new ArrayList<>();
							try {
								for (int i = 0; i < 0; i++) {
									Thread.sleep(1000);
									System.out.println("Sleep "+i);
								}
							}catch(Exception e) {
								System.out.println(e);
							}
							COS_R = 2;
						}
						if (line.contains("END recordping")) {
							templistforSD = new ArrayList<>();
							sdcal = new SDcal(ComListR);
							// sdcal.calSD();
							templistforSD.add(String.valueOf(tempT));
							templistforSD.add(String.valueOf(availableNode.get(availableN)));
							// systemOut("only:" +sdcal.getSD());
							//double tempsd = sdcal.getSD();
							//double tempmean = sdcal.getMean();
							//systemOut("Dou: " + sdcal.SD + " mean: " + tempmean + "\n");
							templistforSD.add(String.valueOf(sdcal.getSD()));
							templistforSD.add(String.valueOf(sdcal.getMean()));
							//systemOut(String.format("T: %s B: %s SD: %s M: %s \n", templistforSD.get(0),
							//		templistforSD.get(1), templistforSD.get(2).toString(),
							//		templistforSD.get(3).toString()));
							ComListR = new ArrayList<>();
							
							try {
								for (int i = 0; i < 0; i++) {
									Thread.sleep(1000);
									System.out.println("Sleep "+i);
								}
							}catch(Exception e) {
								System.out.println(e);
							}
							
							COS_R = 2;
							// systemOut("END^^\n");
							// currentOperation=idle;
						}
					}
					SerialPortInputTransmition = new ArrayList<String>();
					inputToken = new ArrayList<String>();
					break;
				}
				case (2): {
					//System.out.println("templistforSD size:"+templistforSD.size());
					tempListForCAL.add(templistforSD);
					//System.out.println("tempListForCAL size:"+tempListForCAL.size());
					if (!recordcheck) {
						// systemOut("record stage 2\n");
						// systemOut("avN:"+availableN+" size:"+availableNode.size()+"\n");
						if (availableN < availableNode.size() - 1) {
							availableN++;
							COS_R = 0;
						} else {
							// systemOut("tempT:"+tempT+" avN:"+availableN+"
							// avaListF:"+availableNode.get(availableNode.size()-1)+"\n");
							availableN = 0;

							if (tempT < availableNode.get(availableNode.size() - 1)) {
								// systemOut("tempN<availableF\n");
								tempT++;
								// systemOut("n tempT:"+tempT+"\n");
								if (tempT < availableNode.get(availableNode.size() - 1)) {
									COS_R = 0;
								} else if (tempT == availableNode.get(availableNode.size() - 1)) {
									COS_R = 0;
								} else if (tempT > availableNode.get(availableNode.size() - 1)) {
									systemOut("complete record all node RSSI\n");
									COS_R = 3;
								}
							}

						}
					} else {
						systemOut("complete record all node RSSI"+"\nn");
						
						//System.out.println(String.format("recordList:%s T:%s B:%s SD:%s M:%s", null))
						COS_R = 3;
					}

					SerialPortInputTransmition = new ArrayList<String>();
					inputToken = new ArrayList<String>();

					break;
				}
				case (3): {
					systemOut("stage 3 \n");
					systemOut("tempListForCAL size:" + tempListForCAL.size()+"\n");
					systemOut("tempListForCAL.size size:" + tempListForCAL.get(0).size()+"\n");
					
					for(int i=0;i<tempListForCAL.size();i++)
					{
						systemOut(String.format("%s_%s_%.3f_%.3f,", tempListForCAL.get(i).get(0),tempListForCAL.get(i).get(1),
																			Double.parseDouble(tempListForCAL.get(i).get(2)),Double.parseDouble(tempListForCAL.get(i).get(3))));
					}
					System.out.println("");
					// sdcal.printlistUpdateInSDsize();

					// List<List<String>> tempListForCAL=sdcal.SendListToCal();
						
					COS_R = 4;
					currentOperation = idle;
					break;
				}
				case (4): {
					currentOperation = idle;
					break;
				}
				case(5):
				{
					System.out.println("input tempListForCAL by hand? (y/n)");
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					COS_R=6;
					break;
					
				}
				case(6):
				{
					if (!inputToken.isEmpty())
					{
						if(inputToken.get(0).equals("y"))
						{
							System.out.println("input tempListForCAL:");
							COS_R=7;
						}
						else if (inputToken.get(0).equals("n"))
						{
							
							COS_R=0;
						}
						else
						{
							System.out.println("error input");
							COS_R=5;
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case(7):
				{
					if(!inputToken.isEmpty())
					{
						String[] tempStr=inputToken.get(0).split(",");
						for(int i=0 ; i<tempStr.length;i++)
						{
							List<String> templistRecordList=new ArrayList<>();
							String[] tempStr2=tempStr[i].split("_");
							for(int j = 0; j < tempStr2.length;j++)
							{
								templistRecordList.add(tempStr2[j]);
							}
							
							tempListForCAL.add(templistRecordList);
						}
						COS_R=8;
					}
					
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case(8):
				{
					if(!tempListForCAL.isEmpty())
					{
						
						for(int i=0; i<tempListForCAL.size();i++)
						{
							System.out.println(String.format("T:%s B:%s S:%s M:%s",tempListForCAL.get(i).get(0),tempListForCAL.get(i).get(1),
																					tempListForCAL.get(i).get(2),tempListForCAL.get(i).get(3)));
						}
						System.out.println("input complete!!!");
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					currentOperation=idle;
					break;
				}
				
				default: {
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				}
				
				break;
			}

			// discover///////////////////////////////////////
			case (discover): {
				List<float[]> lines;
				switch (COS_D) {
				case (0): {
					availableNode = new ArrayList<Integer>();
					baseX = new ArrayList<Double>();
					baseY = new ArrayList<Double>();
					list = new ArrayList<>();
					lines = new ArrayList<>();
					sendToSerial("discover\n");
					COS_D = 1;
					// currentOperation=idle;
					break;
				}
				case (1): {
					Iterator<String> SerialPortInputTransmitions = SerialPortInputTransmition.iterator();
					while (SerialPortInputTransmitions.hasNext()) {
						String line = SerialPortInputTransmitions.next();
						if (line.contains("Available Nodes: ")) {
							String temp = line.substring(17);
							// String[] nodeAddress = line.substring(line.indexOf(": ") + 2, line.length() -
							// 2).split(", ");
							String[] temp2 = temp.split(",");
							// systemOut("nodeAddress size:"+ temp2.length+"\n");
							for (int index = 0; index < temp2.length - 1; index++) {
								String nodeAddress = temp2[index].trim();
								if (nodeAddress != "" || nodeAddress != " " || nodeAddress != null) 
								{
									availableNode.add(Integer.parseInt(nodeAddress));
								}

							}
						}
						if (line.contains("Total number of ")) {

							// systemOut("availableNode:"+ (availableNode.size())+"\n"); for(int
							// i=0;i<availableNode.size();i++) {
							// systemOut((availableNode.get(i).toString())); }

							// !!!!!!!!!!!!!change is if no. of node>10
							if (availableNode.size() > 0) {
								System.out.println("discover END");
								COS_D = 2;
							} else {
								systemOut("Not enough available node! Return to idle\n");
								COS_D = 0;
								currentOperation = idle;
							}
						}

					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case (2): 
				{
					//System.out.println("Input X Y coordinate? (y/n)");
					//currentOperation = idle;
					System.out.println("re-do discover? (y/n)");
					COS_D = 3;
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case(3):
				{
					if (!inputToken.isEmpty())
					{
						
						if(inputToken.get(0).equals("y"))
						{
							//System.out.println("case 4 input");
							COS_D=0;
						}
						else if (inputToken.get(0).equals("n"))
						{
							//System.out.println("re-do discover? (y/n)");
							COS_D=4;
						}
						else
						{
							System.out.println("error input");
							COS_D=2;
						}
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				case(4):
				{
					//System.out.println("case 4 input");
					System.out.println("idle");
					COS_D=99;
					currentOperation=idle;
					break;
				}
				case(5):
				{
					if (!inputToken.isEmpty())
					{
						if(inputToken.get(0).equals("y"))
						{
							//System.out.println("case 4 input");
							COS_D=0;
						}
						else if (inputToken.get(0).equals("n"))
						{
							System.out.println("idle");
							currentOperation=idle;
							COS_D=99;
						}
						
					}
					inputToken = new ArrayList<String>();
					SerialPortInputTransmition = new ArrayList<String>();
					break;
				}
				}
			}
			
			}

		}

		writeStringBuilderToFile(content, textLog);
		serialPortInputStream.close();
		serialPortOutputStream.close();
		serialPort.close();
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
		f.dispose();
		return toExit;
	}

	private Calculate Calculate(double maxX, double maxY) {
		// TODO Auto-generated method stub
		return null;
	}

	private String String(Integer integer) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SerialReader implements Runnable {
		Scanner localScanner;

		public SerialReader(Scanner scanner) {
			this.localScanner = scanner;
		}

		public void run() {
			while (true) {
				if (localScanner.hasNext()) {
					input = new String(localScanner.nextLine());
					newInput = true;
					if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("end")) {
						break;
					}
				}
			}
		}
	}

	public void sendToSerial(String commandToSend) {
		int commandLength = commandToSend.length();
		for (int i = 0; i < commandLength; i++) {
			try {
				serialPortOutputStream.write((int) commandToSend.charAt(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void systemOut(String messageToUser) {
		System.out.print(messageToUser);
		content.append(">>>>" + messageToUser);
	}

	public static void writeStringBuilderToFile(StringBuilder content, File file) throws IOException {
		FileWriter out = null;
		try {
			out = new FileWriter(file);
			file.createNewFile();
			out.write(content.toString());
			out.close();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
