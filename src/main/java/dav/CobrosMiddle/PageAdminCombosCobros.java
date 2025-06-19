package dav.CobrosMiddle;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import dav.middlePymes.PageServicios;
import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageAdminCombosCobros extends PagePortalPymes {

	public PageAdminCombosCobros(BasePageWeb parentPage) {
		super(parentPage);
	}

	// Pages
	PageServiciosCobros pageServiciosCobros = null;
	PageClienteEmpresaCobros pageClienteEmpresaCobros = null;
	PageEmpresaCobros pageEmpresaCobros;

	/**
	 * Locators Combos
	 * 
	 */
	// Tabla combos
	By tablaCombos = By.xpath("//*[@id='cphCuerpo_gvCombos']//tr");
	By siguienteTabla = By.xpath("//a[contains(text(),'Siguiente')]");

	// Tabla servicios combos
	By tbServiciosCombos = By.xpath("//*[@id='cphCuerpo_gvServiciosCombo']");
	By tablaServiciosCombos = By.xpath("//*[@id='cphCuerpo_gvServiciosCombo']//tr");
	By tituloAdministracion = By.id("lblMasterTitulo");

	// campos necesarios para obtener informaci�n
	By cantidadMaximaTx = By.id("cphCuerpo_ddlECValidacionMaximaTransacciones");
	By cantMaxTxEmpresa = By.id("txtCantidadMaximaEmpresa");

	// tabla donde se encuentra dividido la cantidad maxima de transacciones
	By tablaDestinos = By.xpath("//*[@id='cphCuerpo_UpdatePanel1h']//tr");

	// Tabla de Esquemas de Cobros saldo promedio y transacciones que tiene
	// informaci�n de descuentos dentro del rango o sobre el rango en combos
	By tablaDescuentosRangoCombos = By.xpath("//*[@id='cphCuerpo_tbl_DetalleResSer']//tr");

	// La siguiete tabla se usa si no tiene combos, ira a servicios para verificar
	// la tx con el servicio
	By tablaAdminServicios = By.xpath("//*[@id='cphCuerpo_gvEmpresas']//tr");

	// Strings que se usaran para obtener el destino y cantidad de transacciones
	String tipoDestino = "";
	String destinoParaCombo = "";
	String descuDentroRango = "";
	String descuSobreRango = "";
	String descuPorTxRechazada = "";
	String checkContador = "";

	public static boolean informacionConsultada = false;

	// Almacena los datos
	String[] contarCheckXDestino = null;
	String[] numeroDeTx = null;
	int[] numTxActual = null;

	String[] destinos = { "Davivienda", "Otros Bancos", "Depósitos Electrónicos", "Daviplata",
			"Tarjeta Prepago Maestro" };
	// Elementos que encontraran el input con el valor de descuento
	WebElement descDentroRangoElm = null;
	WebElement descSobreRangoElm = null;
	WebElement descuentoRechazoElm = null;
	WebElement contadorTransacciones = null;

	// se declara esta variable en null porque sive para consultar en cliente
	// empresa y servicio
	WebElement numClienteEmp = null;
	// Este xpath puede cambiar en algunas pantallas, pero con diferente xpath, se
	// vera m�s adelante
	String xpathEditar = "";

	/*
	 * Permite recolectar la informaci�n amedida que va pasando el caso de prueba
	 * especifico
	 */
	List<HashMap<String, String>> results = new ArrayList<>();
	HashMap<String, String> informacion = new HashMap<String, String>();

	// seran de ayuda para reportar eventos en la automatizaci�n
	int eventStatus = Reporter.MIC_PASS;
	String msgReport = "";

	// Nos permitira identificar si el servicio se encuentra en la tabla
	boolean encontrarServicio = false;

	// Nos permitira identificar si el combo se encuentra en la tabla
	boolean encontrarCombo = false;

	/**
	 * Metodo que consulta los combos, busca el servicio para obtener la informaci�n
	 * necesaria para realizar flujos de cobros en front
	 * 
	 * @param combo
	 * @param servicio
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> informacionConCombos(String combo, String servicio) throws Exception {
		this.pageLogin = new PageLoginPymes(this);

		String descuentoServTipoDest = SettingsRun.getTestData().getParameter("Descuento de servicio para tipo destino");
		String descDentroSobreRech = SettingsRun.getTestData().getParameter("Descuento dentro, sobre del rango o por rechazo");
		String porcentDescontar = SettingsRun.getTestData().getParameter("Porcentaje a descontar");

		eventStatus = Reporter.MIC_INFO;
		msgReport = "***** Prueba empresa con combo";
		Reporter.reportEvent(eventStatus, msgReport);

		this.pageEmpresaCobros = new PageEmpresaCobros(pageLogin);
		String comboEncontrado = this.pageEmpresaCobros.comboSeleccionado();

		// se compara el encontrado con lo ingresado en la data
		if (comboEncontrado.equals(combo) || !comboEncontrado.equals("SIN COMBOS")) {
			if (!comboEncontrado.equals(combo)) {
				msgReport = "Los combos no coinciden, pero la empresa cuenta con un combo, se procedera verificando la información";
				eventStatus = Reporter.MIC_WARNING;
				Reporter.reportEvent(eventStatus, msgReport);
				combo = comboEncontrado;
			}
			informacion.put("Verificacion Combo", "Empresa tiene combo");
			// pageServicios retorna el servicio convertido al texto que se encuentra en el
			// portal ya que puede ser diferente al que se encuentra en el excel
			Evidence.save("Evidencia combos");

			// loop que va recorriendo cada tabla
			this.element(By.id("repMasterMenuIzquierdo_tdMasterMenuIzquierdo_2")).click();
			do {
				do {
					DXCUtil.wait(3);
				} while (this.element(tablaCombos) == null);
				// se obtiene filas de la tabla de combos
				List<WebElement> filas = this.findElements(tablaCombos);
				// se recorre cada fila
				for (WebElement fila : filas) {
					// se obtienen las celdas de cada fila
					List<WebElement> celdas = fila.findElements(By.tagName("td"));

					for (WebElement celda : celdas) {
						if (celda.getText().equals(combo)) {
							// se crean los xpath modificados para que sean especificos para que extraiga la
							// informaci�n dependiendo el xpath
							String xpathEstado = "//tr/td[text()='" + celda.getText() + "']/following-sibling::td[1]";
							String xpathSaldoProm = "//tr/td[text()='" + celda.getText()
									+ "']/following-sibling::td/input";

							// luego se crea un String para cada dato que se necesita obtener la informaci�n
							// implementando los string que son xpath modificados
							String estado = this.element(By.xpath(xpathEstado)).getText();
							String saldoPromedio = this.element(By.xpath(xpathSaldoProm)).getAttribute("value");

							// Agregar datos a el hashmap informacion
							informacion.put("Estado Combo", estado);
							informacion.put("Saldo Promedio", saldoPromedio);
							Reporter.reportEvent(eventStatus,
									"Se ha guardado la siguiente información: [ 'Estado Combo' : " + estado + " ]");
							String editarCombo = "//tr/td[text()='" + celda.getText()
									+ "']/following-sibling::td/a[text()='Editar']";
							WebElement btnEditarCombo = this.element(By.xpath(editarCombo));

							do {
								DXCUtil.wait(2);
							} while (btnEditarCombo == null);

							Evidence.save("Evidencia combos");
							btnEditarCombo.click();
							DXCUtil.wait(4);
							encontrarCombo = true;
							break;
						}
					}
					if (encontrarCombo) {
						break;
					}
				}
				if (!encontrarCombo) {
					WebElement siguientePg = this.element(siguienteTabla);
					if (!siguientePg.getAttribute("class").contains("aspNetDisabled")) {
						siguientePg.click();
						Evidence.save("Evidencia combos");
					} else {
						eventStatus = Reporter.MIC_FAIL;
						Reporter.reportEvent(eventStatus,
								"Se esperaba encontrar el combo '" + combo + "', pero no se encontro.");
						SettingsRun.exitTestIteration();
					}

				}
			} while (!encontrarCombo);
			// Una espera hasta que ingrese a administraci�n de combos
			do {
				DXCUtil.wait(5);
			} while (this.element(cantMaxTxEmpresa) == null);
			/*
			 * Obtendremos informaci�n de la pantalla Administraci�n de combos la cual nos
			 * permitira complementar algunas consultas o validaciones en stratus
			 */
			Evidence.save("Evidencia empresa");
			String cantMaxTxEmp = this.element(cantMaxTxEmpresa).getAttribute("value");
			informacion.put("Cantidad Maxima Tx Empresa", cantMaxTxEmp);

			/*
			 * Esta lista esta encargada de dividir el total de las transacciones por
			 * diferentes destinos
			 */
			// Strings que se usaran para obtener el destino y cantidad de transacciones
			String destino = "";
			String cantidadTx = "";
			// se obtiene filas de la tabla de destinos
			List<WebElement> filasDestino = this.findElements(tablaDestinos);
			for (int i = 1; i < filasDestino.size(); i++) {
				// Obtiene el nombre del destino
				destino = filasDestino.get(i).getText();
				// crea un xpath que ayudara a obtener el value del input del destino
				cantidadTx = "//*[@id='cphCuerpo_UpdatePanel1h']//td[contains(text(),'" + destino
						+ "')]/following-sibling::td/input[@value]";
				// se obtiene el value del input
				cantidadTx = this.element(By.xpath(cantidadTx)).getAttribute("value");
				// se adiciona la informaci�n al
				informacion.put("Destino " + destino, destino);
				informacion.put(destino, cantidadTx);
			}
			Evidence.save("Evidencia combos");
			String costoAdmin = this.element(By.id("cphCuerpo_txtCostoAdministracion")).getAttribute("value");
			informacion.put("Costo administracion", costoAdmin);

			// tabla de combos
			WebElement tablaServicios = this.element(tbServiciosCombos);

			eventStatus = Reporter.MIC_PASS;
			if (!tablaServicios.getAttribute("class").contains("aspNetDisabled")) {
				informacionConsultada = true;
				do {
					do {
						DXCUtil.wait(3);
					} while (this.element(tablaServiciosCombos) == null);

					// se obtiene filas de la tabla servicios de combos
					List<WebElement> filas = this.findElements(tablaServiciosCombos);
					// se recorre cada fila
					for (WebElement fila : filas) {
						// se obtienen las celdas de cada fila
						List<WebElement> celdas = fila.findElements(By.tagName("td"));
						for (WebElement celda : celdas) {
							if (servicio.contains("NIT") && celda.getText().contains("NIT")) {
								servicio = celda.getText();
							}
							// Xpath antes de ser usado en la busqueda de un servicio
							String xpathCheck = "//*[@id='cphCuerpo_gvServiciosCombo']//td[text()='" + servicio
									+ "']/following-sibling::td/input[@type='checkbox']";
							String xpathEditar = "//*[@id='cphCuerpo_gvServiciosCombo']//td[text()='" + servicio
									+ "']/following-sibling::td/a";
							if (celda.getText().equals(servicio)) {
								WebElement servicioCheck = this.element(By.xpath(xpathCheck));
								WebElement btnEditar = this.element(By.xpath(xpathEditar));
								if (servicioCheck.isSelected()
										&& !btnEditar.getAttribute("class").contains("aspNetDisabled")) {
									Evidence.save("Evidencia combos");
									btnEditar.click();
									Reporter.reportEvent(eventStatus, "Se ingreso al servicio: [ " + servicio + " ]");
									do {
										DXCUtil.wait(5);
									} while (this.element(tituloAdministracion) == null);
									/**
									 * El siguiente recorre la tabla que va a obtener los porcentajes deacuerdo a
									 * dentro del rango y fuera del rango del tipo destino
									 */
									WebElement checkAplicaDescuento = null;
									Evidence.save("Evidencia combos");

									// se obtiene filas de la tabla de destinos
									List<WebElement> filasSaldosPromyTx = this.findElements(tablaDescuentosRangoCombos);
									// se obtiene filas de la tabla de destinos
									if (!servicio.contains("Servicios")) {
										for (int i = 1; i <= filasSaldosPromyTx.size() - 1; i++) {

											// Almacenara los datos dependiendo la cantidad de informaci�n encontrada
											contarCheckXDestino = new String[filasSaldosPromyTx.size() - 1];

											// Obtiene el nombre del destino
											tipoDestino = filasSaldosPromyTx.get(i).getText().trim();

											if (servicio.equals("Transferencias Cuenta Inscrita")
													|| servicio.equals("Transferencias Cuenta No Inscrita")) {

												// Se arman los xpath para obtener el el atriburo del input
												// descuento dentro del rango
												descuDentroRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[1]/input";
												// descuento sobre del rango
												descuSobreRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[2]/input";
												// descuento por tx rechazada, la tarifa es plena porque no aplica este
												// campo
												descuPorTxRechazada = "0";

												// Este xpath nos ayudara a identificar si el tipo destino tiene activo
												// el
												// contador por transacciones
												checkContador = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[3]/input";
												// Encuentra el elemento y se le asigna un valor para saber si cuenta
												// con el
												// contador o no
												contadorTransacciones = this.element(By.xpath(checkContador));
												if (contadorTransacciones.isSelected()
														&& contadorTransacciones.isDisplayed())
													checkContador = "S";
												else
													checkContador = "N";

												// descuento destino combo, este es un input checkbox, nos ayudara con
												// una
												// validaci�n
												destinoParaCombo = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[4]/input";
												checkAplicaDescuento = this.element(By.xpath(destinoParaCombo));
												DXCUtil.wait(3);
												if (checkAplicaDescuento != null && checkAplicaDescuento.isSelected()
														&& checkAplicaDescuento.isDisplayed())
													destinoParaCombo = "SI";
												else
													destinoParaCombo = "NO";

											} else if (servicio.equals("Transferencias Mismo NIT")) {
												if (tipoDestino.equals("Davivienda")) {
													// Se arman los xpath para obtener el el atriburo del input
													// descuento dentro del rango
													descuDentroRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'Davivienda')]/following-sibling::td[1]/input";
													// descuento sobre del rango
													descuSobreRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'Davivienda')]/following-sibling::td[2]/input";
													// descuento por tx rechazada
													descuPorTxRechazada = "0";

													// Este xpath nos ayudara a identificar si el tipo destino tiene
													// activo
													// el
													// contador por transacciones
													checkContador = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'Davivienda')]/following-sibling::td[3]/input";
													// Encuentra el elemento y se le asigna un valor para saber si
													// cuenta
													// con el
													// contador para transacciones con descuento
													contadorTransacciones = this.element(By.xpath(checkContador));
													if (contadorTransacciones.isSelected()
															&& contadorTransacciones.isDisplayed())
														checkContador = "S";
													else
														checkContador = "N";

													// descuento destino combo, este es un input checkbox, nos ayudara
													// con
													// una
													// validaci�n
													destinoParaCombo = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'Davivienda')]/following-sibling::td[4]/input";
													checkAplicaDescuento = this.element(By.xpath(destinoParaCombo));

													if (checkAplicaDescuento != null
															&& checkAplicaDescuento.isSelected()
															&& checkAplicaDescuento.isDisplayed())
														destinoParaCombo = "SI";
													else
														destinoParaCombo = "NO";

													// Encuentra elemento del tipo destino y luego se obtiene el
													// atributo
													// que se encuentra dentro del rango
													descDentroRangoElm = this.element(By.xpath(descuDentroRango));
													descuDentroRango = descDentroRangoElm.getAttribute("value");

													// Encuentra elemento del tipo destino y luego se obtiene el
													// atributo
													// que se encuentra sobre del rango
													descSobreRangoElm = this.element(By.xpath(descuSobreRango));
													descuSobreRango = descSobreRangoElm.getAttribute("value");

													informacion.put("Tipo destino " + tipoDestino, tipoDestino);
													informacion.put("Descuento dentro rango " + tipoDestino,
															descuDentroRango);
													informacion.put("Descuento sobre rango " + tipoDestino,
															descuSobreRango);
													informacion.put("Descuento por Tx rechazada " + tipoDestino,
															descuPorTxRechazada);
													informacion.put("Descuento para combo " + tipoDestino,
															destinoParaCombo);
													informacion.put("Aplica contador " + tipoDestino, checkContador);

												}
												break;
											} else {

												// Se arman los xpath para obtener el el atriburo del input
												// descuento dentro del rango
												descuDentroRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[1]/input";
												// descuento sobre del rango
												descuSobreRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[2]/input";
												// descuento por tx rechazada
												descuPorTxRechazada = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[3]/input";

												// Este xpath nos ayudara a identificar si el tipo destino tiene activo
												// el
												// contador por transacciones
												checkContador = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[4]/input";
												// Encuentra el elemento y se le asigna un valor para saber si cuenta
												// con el
												// contador o no
												contadorTransacciones = this.element(By.xpath(checkContador));
												if (contadorTransacciones.isSelected()
														&& contadorTransacciones.isDisplayed())
													checkContador = "S";
												else
													checkContador = "N";

												// descuento destino combo, este es un input checkbox, nos ayudara con
												// una
												// validaci�n
												destinoParaCombo = "//*[@id='cphCuerpo_tbl_DetalleResSer']//td[contains(text(),'"
														+ tipoDestino + "')]/following-sibling::td[5]/input";
												checkAplicaDescuento = this.element(By.xpath(destinoParaCombo));
												DXCUtil.wait(3);
												if (checkAplicaDescuento != null && checkAplicaDescuento.isSelected()
														&& checkAplicaDescuento.isDisplayed())
													destinoParaCombo = "SI";
												else
													destinoParaCombo = "NO";
											}

											WebElement ingresarDescu = null;
											// este action nos permitira hacer algunas funciones con el teclado, porque
											// el
											// campo con el que vamos a interactuar no permite realizar un clear() y
											// sendkeys() por lo cual usaremos atajos del teclado
											Actions action = new Actions(getDriver());
											// Obtiene el toolkit
											Toolkit toolKit = Toolkit.getDefaultToolkit();
											// Ayuda a guardar en el portapapeles
											Clipboard clipBoard = toolKit.getSystemClipboard();

											if ((descuentoServTipoDest == null || descuentoServTipoDest.isEmpty()
													|| descuentoServTipoDest.equals("")
													|| descuentoServTipoDest.equals(" "))
													&& !descuentoServTipoDest.equals("No realizar cambios")) {
												if (tipoDestino.equals(descuentoServTipoDest)) {
													if (encontrarCombo) {
														switch (descDentroSobreRech) {
														case "Descuento dentro del rango":
															ingresarDescu = this.element(By.xpath(descuDentroRango));
															break;

														case "Descuento sobre el rango":
															ingresarDescu = this.element(By.xpath(descuSobreRango));
															break;

														case "Descuento por rechazo":
															ingresarDescu = this.element(By.xpath(descuPorTxRechazada));
															break;

														default:
															break;
														}
														// esto representa que selecciono el texto
														StringSelection stringSelection = new StringSelection(
																porcentDescontar);
														// Copia en el portapapeles
														clipBoard.setContents(stringSelection, null);
														// se da click en el input en el que ingresaremos la informaci�n
														if (ingresarDescu != null) {
															ingresarDescu.click();
															// Indicamos que es un(Control + a) para seleccionar el
															// texto
															action.keyDown(Keys.CONTROL).sendKeys("a")
																	.keyUp(Keys.CONTROL).perform();
															// Indicamos que es un(Control + a) para pegar el texto del
															// clipboard
															action.keyDown(Keys.CONTROL).sendKeys("v")
																	.keyUp(Keys.CONTROL).perform();
															Evidence.save("Evidencia combos");
															action.keyDown(Keys.ALT).keyUp(Keys.ALT).perform();

															Reporter.reportEvent(Reporter.MIC_INFO,
																	"Se ha cambiado el descuento ");
														}
													}
												}
											}

											try {

												// Encuentra elemento del tipo destino y luego se obtiene el atributo
												// que se encuentra dentro del rango
												descDentroRangoElm = this.element(By.xpath(descuDentroRango));
												descuDentroRango = descDentroRangoElm.getAttribute("value");

												// Encuentra elemento del tipo destino y luego se obtiene el atributo
												// que se encuentra sobre del rango
												descSobreRangoElm = this.element(By.xpath(descuSobreRango));
												descuSobreRango = descSobreRangoElm.getAttribute("value");

												// Encuentra elemento del tipo destino rechazado y luego se obtiene el
												// atributo
												if (!descuPorTxRechazada.equals("0")) {
													descuentoRechazoElm = this.element(By.xpath(descuPorTxRechazada));
													descuPorTxRechazada = descuentoRechazoElm.getAttribute("value");
												}

											} catch (Exception e) {
												if (descDentroRangoElm == null)
													descuDentroRango = "0";

												if (descSobreRangoElm == null)
													descuSobreRango = "0";

												if (descuentoRechazoElm == null)
													descuPorTxRechazada = "0";
											}

											informacion.put("Tipo destino " + tipoDestino, tipoDestino);
											informacion.put("Descuento dentro rango " + tipoDestino, descuDentroRango);
											informacion.put("Descuento sobre rango " + tipoDestino, descuSobreRango);
											informacion.put("Descuento por Tx rechazada " + tipoDestino,
													descuPorTxRechazada);
											informacion.put("Descuento para combo " + tipoDestino, destinoParaCombo);
											informacion.put("Aplica contador " + tipoDestino, checkContador);
										}
									} else {
										for (int i = 1; i < filasSaldosPromyTx.size(); i++) {
											if (servicio.equals("Pago de Servicios")) {
												tipoDestino = servicio;
												// Se arman los xpath para obtener el el atriburo del input
												// descuento dentro del rango
												descuDentroRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//tr/td[1]/input";
												// descuento sobre del rango
												descuSobreRango = "//*[@id='cphCuerpo_tbl_DetalleResSer']//tr/td[2]/input";
												// descuento por tx rechazada, la tarifa es plena porque no aplica este
												// campo
												descuPorTxRechazada = "0";

												// Este xpath nos ayudara a identificar si el tipo destino tiene activo
												// el
												// contador por transacciones
												checkContador = "//*[@id='cphCuerpo_tbl_DetalleResSer']//tr/td[3]/input";
												// Encuentra el elemento y se le asigna un valor para saber si cuenta
												// con el
												// contador o no

												contadorTransacciones = this.element(By.xpath(checkContador));
												if (contadorTransacciones.isSelected()
														&& contadorTransacciones.isDisplayed())
													checkContador = "S";
												else
													checkContador = "N";

												// descuento destino combo, este es un input checkbox, nos ayudara con
												// una
												// validaci�n
												destinoParaCombo = "//*[@id='cphCuerpo_tbl_DetalleResSer']//tr/td[4]/input";
												checkAplicaDescuento = this.element(By.xpath(destinoParaCombo));
												DXCUtil.wait(3);
												if (checkAplicaDescuento != null && checkAplicaDescuento.isSelected()
														&& checkAplicaDescuento.isDisplayed())
													destinoParaCombo = "SI";
												else
													destinoParaCombo = "NO";

												// Encuentra elemento del tipo destino y luego se obtiene el atributo
												// que se encuentra dentro del rango
												descDentroRangoElm = this.element(By.xpath(descuDentroRango));
												descuDentroRango = descDentroRangoElm.getAttribute("value");

												// Encuentra elemento del tipo destino y luego se obtiene el atributo
												// que se encuentra sobre del rango
												descSobreRangoElm = this.element(By.xpath(descuSobreRango));
												descuSobreRango = descSobreRangoElm.getAttribute("value");

												informacion.put("Tipo destino " + tipoDestino, tipoDestino);
												informacion.put("Descuento dentro rango " + tipoDestino,
														descuDentroRango);
												informacion.put("Descuento sobre rango " + tipoDestino,
														descuSobreRango);
												informacion.put("Descuento por Tx rechazada " + tipoDestino,
														descuPorTxRechazada);
												informacion.put("Descuento para combo " + tipoDestino,
														destinoParaCombo);
												informacion.put("Aplica contador " + tipoDestino, checkContador);
												break;
											}
										}
									}
									eventStatus = Reporter.MIC_INFO;
									Reporter.reportEvent(eventStatus,
											"Información de descuento por destino en administración de combos se ha almacenado como se esperaba.");
									Evidence.save("Evidencia combos");
									WebElement btnGrabar = this.element(By.id("cphCuerpo_btnContinuar"));
									btnGrabar.click();
									DXCUtil.wait(3);
									this.acceptDialog();
									encontrarServicio = true;
									break;
								} else {
									eventStatus = Reporter.MIC_INFO;
									Reporter.reportEvent(eventStatus, "El check del servicio " + servicio
											+ " no se encuentra seleccionado y no es posible editar.");

									for (int i = 0; i < destinos.length; i++) {

										// Obtiene el nombre del destino
										descuDentroRango = "0";
										descuSobreRango = "0";
										descuPorTxRechazada = "0";

										informacion.put("Tipo destino " + destinos[i], destinos[i]);
										informacion.put("Descuento dentro rango " + destinos[i], descuDentroRango);
										informacion.put("Descuento sobre rango " + destinos[i], descuSobreRango);
										informacion.put("Descuento por Tx rechazada " + destinos[i],
												descuPorTxRechazada);
										informacion.put("Descuento para combo " + destinos[i], "NO");
										informacion.put("Aplica contador " + destinos[i], "N");
									}
								}
							}
						}
						if (encontrarServicio) {
							break;
						}
					}
					if (!encontrarServicio) {
						WebElement siguientePg = this.element(siguienteTabla);

						if (!siguientePg.getAttribute("class").contains("aspNetDisabled")) {
							siguientePg.click();
							DXCUtil.wait(5);
							Evidence.save("Evidencia combos");
						} else {
							eventStatus = Reporter.MIC_INFO;
							Reporter.reportEvent(eventStatus,
									"Se esperaba encontrar el servicio " + servicio + ", pero no se encontro.");
							SettingsRun.exitTestIteration();
						}
					}
				} while (!encontrarServicio);

			} else {
				// Ingresa a los servicios para consultar el servicio y validar la informaci�n
				// del descuento
				this.pageServiciosCobros = new PageServiciosCobros(pageLogin);
				informacionConsultada = true;
				informacion = this.pageServiciosCobros.consultarServicio(informacion, servicio);
			}

			// se compara el encontrado con lo ingresado en la data
			if (comboEncontrado.equals(combo) || !comboEncontrado.equals("SIN COMBOS")) {
				this.pageEmpresaCobros.consultarEmpresa();
				this.pageEmpresaCobros.comparacionComboEmpresa(eventStatus, msgReport, comboEncontrado, combo, results,
						informacion);
			} else {
				eventStatus = Reporter.MIC_INFO;
				msgReport = "Se esperaba que el combo '" + combo + "', pero se presento: " + comboEncontrado;
				Reporter.reportEvent(eventStatus, msgReport);

				// Ingresa a los servicios para consultar el servicio y validar la informaci�n
				// del descuento
				this.pageServiciosCobros = new PageServiciosCobros(pageLogin);
				informacion = this.pageServiciosCobros.consultarServicio(informacion, servicio);
			}

			this.element(By.id("repMasterMenuIzquierdo_tdMasterMenuIzquierdo_0")).click();
			this.pageClienteEmpresaCobros = new PageClienteEmpresaCobros(pageLogin);
			this.pageClienteEmpresaCobros.consultaClienteEmpresa(numClienteEmp, xpathEditar, results, informacion,
					eventStatus, msgReport);
			Evidence.save("Evidencia empresa");

			// una vez encontrada toda la informaci�n, se agrega al dato "result" para
			// usarla en el en otros procesos
			results.add(informacion);
			eventStatus = Reporter.MIC_INFO;
			Reporter.reportEvent(eventStatus, "Se ha almacenado la información de middle correctamente.\n");

			// devuelve toda la informaci�n almacenada
		} else {
			eventStatus = Reporter.MIC_WARNING;
			Reporter.reportEvent(eventStatus, "**** No se esperaba empresa SIN COMBOS.\n");
			informacion.put("Verificacion Combo", "Empresa no tiene combo");
			informacion.put("Empresa sin combo", comboEncontrado);
			results.add(informacion);
		}
		return results;
	}

	/**
	 * Metodo que consulta los servicios, busca el servicio para obtener la
	 * informaci�n necesaria para realizar flujos de cobros en front
	 * 
	 * @param tipoCobro
	 * @param servicio
	 * @return
	 * @throws Exception
	 */

	public List<HashMap<String, String>> informacionSinCombos(String servicio, String combo) throws Exception {
		this.pageClienteEmpresaCobros = new PageClienteEmpresaCobros(pageLogin);
		this.pageServiciosCobros = new PageServiciosCobros(pageLogin);
		this.pageEmpresaCobros = new PageEmpresaCobros(pageLogin);

		eventStatus = Reporter.MIC_INFO;
		msgReport = "***** Prueba empresa sin combo";
		Reporter.reportEvent(eventStatus, msgReport);

		// Se verifica que no tenga un combo
		String comboEncontrado = this.pageEmpresaCobros.comboSeleccionado();
		if (comboEncontrado.equals("SIN COMBOS")) {
			informacion.put("Verificacion Combo", "Empresa no tiene combo");
			// pageServicios retorna el servicio convertido al texto que se encuentra en el
			// portal ya que puede ser diferente al que se encuentra en el excel
			Evidence.save("Evidencia cliente empresarial");
			int contador = 1;
			do {
				contador++;
				DXCUtil.wait(1);
				if (contador > 30) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut");
					SettingsRun.exitTestIteration();
				}
			} while (this.element(tituloAdministracion) == null);

			// Se consulta por n�mero de cliente
			this.pageClienteEmpresaCobros.consultaClienteEmpresa(numClienteEmp, xpathEditar, results, informacion,
					eventStatus, msgReport);

			// por medio de este bot�n ingresa a servicios a almacenal la informaci�n
			// restante
			// Ingresa a los servicios para consultar el servicio y validar la informaci�n
			// del descuento
			informacion = this.pageServiciosCobros.consultarServicio(informacion, servicio);
			Evidence.save("Evidencia servicios");

			// una vez encontrada toda la informaci�n, se agrega al dato "result" para
			// usarla en el en otros procesos
			results.add(informacion);
			eventStatus = Reporter.MIC_INFO;
			Reporter.reportEvent(eventStatus, "Se ha almacenado la información de middle correctamente.\n");
			// devuelve toda la informaci�n almacenada
		} else {
//			results = this.informacionConCombos(combo, servicio);
			eventStatus = Reporter.MIC_WARNING;
			Reporter.reportEvent(eventStatus, "**** No se esperaba el combo " + comboEncontrado + ".\n");
			informacion.put("Verificacion Combo", "Empresa tiene combo");
			informacion.put("Combo encontrado no diligenciado", comboEncontrado);
			results.add(informacion);
		}
		return results;
	}

	/**
	 * Metodo que guarda la informaci�n obtenida en administraci�n de combos y
	 * devuelve la informaci�n, solicita estos patametros:
	 * 
	 * 
	 * @param combo
	 * @param tipoCobro
	 * @param servicio
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getResultsConCombo(String combo, String servicio) throws Exception {
		results = informacionConCombos(combo, servicio);
		return results;
	}

	/**
	 * Metodo que guarda la informaci�n obtenida en administraci�n de servicios y
	 * devuelve la informaci�n, solicita estos patametros:
	 * 
	 * @param tipoCobro
	 * @param servicio
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getResultsSinCombo(String servicio, String combo) throws Exception {
		results = informacionSinCombos(servicio, combo);
		return results;
	}

	/**
	 * Este metodo retorna un bool que nos hara saber si se consulto administraci�n
	 * de combos o servicios
	 * 
	 * @return
	 */
	public static boolean getInformacionConsultada() {
		return informacionConsultada;
	}

}
