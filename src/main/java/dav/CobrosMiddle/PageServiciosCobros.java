package dav.CobrosMiddle;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.middlePymes.PageInicioMiddle;
import dav.pymes.PageLoginPymes;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageServiciosCobros extends PageInicioMiddle {

	public PageServiciosCobros(PageLoginPymes parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Locators o elementos
	 */
	// Tabla donde se encuentran los servicios
	By tablaServiciosPortal = By.xpath("//*[@id='cphCuerpo_gvServicio']//tr");
	By btnSiguienteTabla = By.xpath("//a[contains(text(),'Siguiente')]");

	// Tabla de Esquemas de Cobros saldo promedio y transacciones que tiene
	// informaci�n de descuentos dentro del rango o sobre el rango en servicios
	By tablaDescuentosServicio = By.xpath("//*[@id='cphCuerpo_tbl_Servicio']//tr");
	By tablaDescuentosPagoServicios = By.xpath("//*[@id='cphCuerpo_tbl_Consulta']//tr");

	By inpNumIdentificacion = By.id("cphCuerpo_txtBQNumIdentificacionEmpresa");
	By inpNumClienteEmpresa = By.id("cphCuerpo_txtNoClienteEmpresa");

	By linkServicios = By.id("repMasterMenuIzquierdo_tdMasterMenuIzquierdo_3");
	By btnConsultar = By.id("cphCuerpo_btnBuscar");

	By labelSegmento = By.id("cphCuerpo_lblSegmento");

	// Elementos que encontraran el input con el valor de descuento
	WebElement descDentroRangoElm = null;
	WebElement descSobreRangoElm = null;
	WebElement descuentoRechazoElm = null;
	WebElement contadorTransacciones = null;
	/*
	 * -----------------------------------------------------------------------------
	 */

	// seran de ayuda para reportar eventos en la automatizaci�n
	int eventStatus = Reporter.MIC_PASS;
	String msgReport = "";

	// Strings que se usaran para obtener el destino y cantidad de transacciones
	String tipoDestino = "";
	String destinoParaCombo = "";
	String descuDentroRango = "";
	String descuSobreRango = "";
	String descuPorTxRechazada = "";
	String checkContador = "";

	// Este xpath puede cambiar en algunas pantallas, pero con diferente xpath, se
	// vera m�s adelante
	String xpathEditar = "";

	String[] destinos = { "Davivienda", "Otros Bancos", "Dep�sitos Electr�nicos", "Daviplata",
			"Tarjeta Prepago Maestro" };

	/**
	 * Permite realizar la consulta del servicio al cual se etraera la informaci�n
	 * sobre los descuestos que se aplicaran a cada tipo destino encontado, este
	 * metodo unicamente se encarga de extraer y almacenar informaci�n, no se
	 * modificara ningun servicio en el caso de los descuentos ya que no aplica para
	 * este apartado de servicios
	 * 
	 * @param servicio
	 * @param encontrarServicio
	 * @throws Exception
	 */
	public HashMap<String, String> consultarServicio(HashMap<String, String> informacion, String servicio)
			throws Exception {

		boolean encontrarServicio = false;
		// Se cambia el servicio porque en el apartado de servicios cambia el nombre a
		// como se esta declarando
		if (servicio.contains("Mismo NIT") || servicio.contains("TC")) {
			servicio = "Transferencia NIT Propio";
		}

		String clienteEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial");
		String nitEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa");
		eventStatus = Reporter.MIC_PASS;

		// por medio de este bot�n ingresa a servicios a almacenal la informaci�n
		// restante

		this.findElement(linkServicios).click();

		WebElement numIdentificacion = this.findElement(inpNumIdentificacion);
		WebElement numClienteEmpresa = this.findElement(inpNumClienteEmpresa);

		numIdentificacion.sendKeys(nitEmpresa);
		numClienteEmpresa.sendKeys(clienteEmpresa);

		WebElement btnBuscar = this.findElement(btnConsultar);
		btnBuscar.click();

		// Consulta la empresa de la cual se extraera informaci�n
		WebElement btnContrtServs = null;

		// se crea el xpath que ayudara a consultar el cliente
		String xpathContrtServs = "//*[@id='cphCuerpo_gvEmpresas']//td[contains(text(),'" + nitEmpresa
				+ "')]/../td[contains(text(),'" + clienteEmpresa + "')]/../td/a";
		btnContrtServs = this.findElement(By.xpath(xpathContrtServs));

		if (btnContrtServs != null) {
			Evidence.save("Evidencia servicios");
			btnContrtServs.click();
		} else {
			Reporter.reportEvent(Reporter.MIC_INFO, "No se encontr� ningun resultado del cliente empresa: "
					+ clienteEmpresa + " o  nit: " + nitEmpresa + ".");
			SettingsRun.exitTestIteration();
		}
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				Evidence.save("No se encuentra el segmento ");
				this.terminarIteracion();
			}

		} while (this.element(labelSegmento) == null);

		// se obtiene el segmento que se eucuentra visible
		String segmento = this.findElement(labelSegmento).getText();
		// Se adiciona el segmento encontrado
		informacion.put("Segmento", segmento);
		eventStatus = Reporter.MIC_INFO;
		Reporter.reportEvent(eventStatus, "Se ha guardado la siguiente información: [ 'Segmento' : " + segmento + " ]");
		// Xpath antes de ser usado en la busqueda de un servicio
		String xpathCheck = "//*[@id='cphCuerpo_gvServicio']//td[contains(text(),'" + servicio
				+ "')]/following-sibling::td/span/input[@type='checkbox']";
		xpathEditar = "//*[@id='cphCuerpo_gvServicio']//td[contains(text(),'" + servicio
				+ "')]/following-sibling::td/a";

		WebElement btnEditar = null;
		// se buscara en la tabla el servicio
		do {
			contador = 0;
			do {
				contador++;
				DXCUtil.wait(1);
				if (contador >= 30) {
					Evidence.save("No se encuentra el titulo de Administracion");
					this.terminarIteracion();
				}

			} while (this.element(tablaServiciosPortal) == null);
			// se recorre cada fila
			List<WebElement> filas = this.findElements(tablaServiciosPortal);
			for (WebElement fila : filas) {
				// se obtienen las celdas de cada fila
				List<WebElement> celdas = fila.findElements(By.tagName("td"));
				for (WebElement celda : celdas) {
					if (celda.getText().equals(servicio)) {
						WebElement servicioCheck = this.findElement(By.xpath(xpathCheck));
						try {
							btnEditar = this.findElement(By.xpath(xpathEditar));
						} catch (NoSuchElementException e) {
							Reporter.reportEvent(Reporter.MIC_FAIL,
									"Se esperaba editar el servicio, pero no fue posible editar");
							SettingsRun.exitTestIteration();
						}
						if (servicioCheck.isSelected() && btnEditar != null) {
							Evidence.saveFullPage("Evidencia servicios", this);
							btnEditar.click();
							DXCUtil.wait(5);
							Reporter.reportEvent(eventStatus, "Se ingreso al servicio: [ " + servicio + " ]");
							encontrarServicio = true;
							break;
						} else {
							Reporter.reportEvent(Reporter.MIC_FAIL, "El servicio " + servicio
									+ " no se encuentra seleccionado o no fue posible editar.");
							SettingsRun.exitTestIteration();
						}
					}
				}
				if (encontrarServicio) {
					break;
				}
			}
			if (!encontrarServicio) {
				WebElement siguientePg = this.findElement(btnSiguienteTabla);

				if (!siguientePg.getAttribute("class").contains("aspNetDisabled")) {
					Evidence.saveFullPage("Evidencia servicios", this);
					String inicial = this
							.getText(By.xpath("//select[@id='cphCuerpo_dropDPagina']//option[@selected='selected']"));
					siguientePg.click();
					String cambio = "";
					do {
						DXCUtil.wait(1);
						cambio = this.getText(
								By.xpath("//select[@id='cphCuerpo_dropDPagina']//option[@selected='selected']"));

					} while (inicial.equals(cambio));

				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "Se esperaba encontrar el servicio: [ " + servicio + " ].");
					SettingsRun.exitTestIteration();
				}
			}
		} while (!encontrarServicio);
		Evidence.save("Evidencia servicios");

		WebElement tablaDescuentosServicios = null;
		contador = 0;

		tablaDescuentosServicios = this.element(tablaDescuentosServicio);

		if (tablaDescuentosServicios == null) {
			tablaDescuentosServicios = this.element(tablaDescuentosPagoServicios);
			if (tablaDescuentosServicios == null) {
				Evidence.save("No se encuentra la Tabla de Descuentos de Servicios ");
			}
		}

		if (!servicio.contains("Pagos a créditos de terceros") && !servicio.contains("AFC")
				&& !servicio.contains("Automático")) {

			do {
				contador++;
				DXCUtil.wait(3);
				if (contador >= 30) {
					Evidence.save("No se encuentra la Tabla de Descuentos de Servicios ");
					this.terminarIteracion();
				}

			} while (this.element(tablaDescuentosServicios) == null);

		}

		Evidence.save("Evidencia servicios");
		if (!servicio.contains("Pagos a créditos de terceros") && !servicio.contains("AFC")
				&& !servicio.contains("Automático")) {
			do {
				DXCUtil.wait(2);
			} while (tablaDescuentosServicios == null);
		}

		if (tablaDescuentosServicios != null && tablaDescuentosServicios.isDisplayed()) {
			// se obtiene filas de la tabla de destinos
			List<WebElement> filasSaldosPromyTx = null;

			// se obtiene filas de la tabla de destinos
			if (!servicio.contains("Servicios") && !servicio.contains("Automático")) {
				filasSaldosPromyTx = this.findElements(tablaDescuentosServicio);

				for (int i = 1; i <= filasSaldosPromyTx.size() - 1; i++) {
					// Obtiene el nombre del destino
					tipoDestino = filasSaldosPromyTx.get(i).getText().trim();

					if (servicio.equals("Transferencias Cuenta Inscrita")
							|| servicio.equals("Transferencias Cuenta No Inscrita")) {
						// xpath que ubican el porcentaje de descuento por tipo destino
						descuDentroRango = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
								+ "')]/following-sibling::td[1]/input";
						descuSobreRango = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
								+ "')]/following-sibling::td[2]/input";
						descuPorTxRechazada = "0";

						// Este xpath nos ayudara a identificar si el tipo destino tiene activo el
						// contador por transacciones
						checkContador = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
								+ "')]/following-sibling::td[3]//input";
						// Encuentra el elemento y se le asigna un valor para saber si cuenta con el
						// contador o no
						contadorTransacciones = this.findElement(By.xpath(checkContador));
						if (contadorTransacciones.isSelected() && contadorTransacciones.isDisplayed())
							checkContador = "S";
						else
							checkContador = "N";
					} else if (servicio.contains("Transferencia NIT Propio")) {
						if (tipoDestino.equals("Davivienda")) {
							// xpath que ubican el porcentaje de descuento por tipo destino
							descuDentroRango = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'Davivienda')]/following-sibling::td[1]//input";
							descuSobreRango = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'Davivienda')]/following-sibling::td[2]//input";
							descuPorTxRechazada = "0";

							// Este xpath nos ayudara a identificar si el tipo destino tiene activo el
							// contador por transacciones que aplican descuento
							checkContador = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'Davivienda')]/following-sibling::td[3]//input";
							// Encuentra el elemento y se le asigna un valor para saber si cuenta con el
							// contador o no
							contadorTransacciones = this.findElement(By.xpath(checkContador));
							if (contadorTransacciones.isSelected() && contadorTransacciones.isDisplayed())
								checkContador = "S";
							else
								checkContador = "N";

						}
						// Encuentra elemento del tipo destino y luego se obtiene el atributo
						// que se encuentra dentro del rango
						descDentroRangoElm = this.findElement(By.xpath(descuDentroRango));
						descuDentroRango = descDentroRangoElm.getAttribute("value");

						// Encuentra elemento del tipo destino y luego se obtiene el atributo
						// que se encuentra sobre del rango
						descSobreRangoElm = this.findElement(By.xpath(descuSobreRango));
						descuSobreRango = descSobreRangoElm.getAttribute("value");

						informacion.put("Tipo destino " + tipoDestino, tipoDestino);
						informacion.put("Descuento dentro rango " + tipoDestino, descuDentroRango);
						informacion.put("Descuento sobre rango " + tipoDestino, descuSobreRango);
						informacion.put("Descuento por Tx rechazada " + tipoDestino, descuPorTxRechazada);
						informacion.put("Aplica contador " + tipoDestino, checkContador);
						break;
					} else {

						// xpath que ubican el porcentaje de descuento por tipo destino
						descuDentroRango = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
								+ "')]/following-sibling::td[1]/input";
						descuSobreRango = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
								+ "')]/following-sibling::td[2]/input";
						descuPorTxRechazada = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
								+ "')]/following-sibling::td[3]/input";

						// Este xpath nos ayudara a identificar si el tipo destino tiene activo el
						// contador por transacciones
						if (servicio.contains("Propios")) {
							checkContador = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
									+ "')]/following-sibling::td[3]//input";
						} else {
							checkContador = "//*[@id='cphCuerpo_tbl_Servicio']//td[contains(text(),'" + tipoDestino
									+ "')]/following-sibling::td[4]//input";
						}

						// Encuentra el elemento y se le asigna un valor para saber si cuenta con el
						// contador o no
						contadorTransacciones = this.findElement(By.xpath(checkContador));
						if (contadorTransacciones.isSelected() && contadorTransacciones.isDisplayed())
							checkContador = "S";
						else
							checkContador = "N";
					}

					try {
						// Encuentra elemento del tipo destino y luego se obtiene el atributo
						// que se encuentra dentro del rango
						descDentroRangoElm = this.findElement(By.xpath(descuDentroRango));
						descuDentroRango = descDentroRangoElm.getAttribute("value");

						// Encuentra elemento del tipo destino y luego se obtiene el atributo
						// que se encuentra sobre del rango
						descSobreRangoElm = this.findElement(By.xpath(descuSobreRango));
						descuSobreRango = descSobreRangoElm.getAttribute("value");

						// Encuentra elemento del tipo destino rechazado y luego se obtiene el atributo
						if (!descuPorTxRechazada.equals("0")) {
							descuentoRechazoElm = this.findElement(By.xpath(descuPorTxRechazada));
							descuPorTxRechazada = descuentoRechazoElm.getAttribute("value");
						}

					} catch (Exception e) {
						if (descDentroRangoElm == null) {
							descuDentroRango = "0";
						}
						if (descSobreRangoElm == null) {
							descuSobreRango = "0";
						}
						if (descuentoRechazoElm == null) {
							descuPorTxRechazada = "0";
						}

					}
					informacion.put("Tipo destino " + tipoDestino, tipoDestino);
					informacion.put("Descuento dentro rango " + tipoDestino, descuDentroRango);
					informacion.put("Descuento sobre rango " + tipoDestino, descuSobreRango);
					informacion.put("Descuento por Tx rechazada " + tipoDestino, descuPorTxRechazada);
					informacion.put("Aplica contador " + tipoDestino, checkContador);
				}
			} else {
				filasSaldosPromyTx = this.findElements(tablaDescuentosPagoServicios);
				for (int i = 1; i < filasSaldosPromyTx.size(); i++) {

					if (servicio.equals("Pago de Servicios")) {
						tipoDestino = servicio;
						// Se arman los xpath para obtener el el atriburo del input
						// descuento dentro del rango
						descuDentroRango = "//*[@id='cphCuerpo_tbl_Consulta']//tr/td[1]/input";
						// descuento sobre del rango
						descuSobreRango = "//*[@id='cphCuerpo_tbl_Consulta']//tr/td[2]/input";
						// descuento por tx rechazada, la tarifa es plena porque no aplica este campo
						descuPorTxRechazada = "0";

						// Este xpath nos ayudara a identificar si el tipo destino tiene activo el
						// contador por transacciones
						checkContador = "//*[@id='cphCuerpo_tbl_Consulta']//tr/td[3]/input";

						// Encuentra elemento del tipo destino y luego se obtiene el atributo
						// que se encuentra dentro del rango
						descDentroRangoElm = this.findElement(By.xpath(descuDentroRango));
						descuDentroRango = descDentroRangoElm.getAttribute("value");

						// Encuentra elemento del tipo destino y luego se obtiene el atributo
						// que se encuentra sobre del rango
						descSobreRangoElm = this.findElement(By.xpath(descuSobreRango));
						descuSobreRango = descSobreRangoElm.getAttribute("value");

						// Encuentra el elemento y se le asigna un valor para saber si cuenta con el
						// contador o no
						contadorTransacciones = this.findElement(By.xpath(checkContador));
						if (contadorTransacciones.isSelected() && contadorTransacciones.isDisplayed())
							checkContador = "S";
						else
							checkContador = "N";

						informacion.put("Tipo destino " + tipoDestino, tipoDestino);
						informacion.put("Descuento dentro rango " + tipoDestino, descuDentroRango);
						informacion.put("Descuento sobre rango " + tipoDestino, descuSobreRango);
						informacion.put("Descuento por Tx rechazada " + tipoDestino, descuPorTxRechazada);
						informacion.put("Aplica contador " + tipoDestino, checkContador);
						break;
					}
				}
			}
		} else {
			for (int j = 0; j < destinos.length; j++) {
				informacion.put("Tipo destino " + destinos[j], destinos[j]);
				informacion.put("Descuento dentro rango " + destinos[j], "0");
				informacion.put("Descuento sobre rango " + destinos[j], "0");
				informacion.put("Descuento por Tx rechazada " + destinos[j], "0");
				informacion.put("Aplica contador " + destinos[j], "N");

			}
		}
		Evidence.save("Evidencia servicios");
		Reporter.reportEvent(eventStatus,
				"Información de descuento por destino en servicios se ha almacenado como se esperaba.");
		return informacion;
	}
}
