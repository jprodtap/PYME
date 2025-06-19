package dav.FiduciaEstrusturada;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.FiduciaEstrusturada.PageFiduciaFront;
import dav.pymes.moduloCrearTx.PageOrigen;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;

import dxc.util.DXCUtil;

public class PageFormatosyDetalles extends BasePageWeb {

	PageOrigen pageOrigen = null;
	PageFiduciaFront pageFiduciaFront = null;

	public PageFormatosyDetalles(BasePageWeb parentPage) {
		super(parentPage);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Locators preventa
	 */
	String linkDetalles = "//table/tbody//td[contains(text(),'{PROYECTO}')]/following-sibling::td/a";
	By rutasFiducia = By.xpath("//div[2]/div[3]/div[1]");

	By tableDetalles = By.xpath("//table[@id='table_presale']/tbody");
	By tableDetalles2 = By.xpath("//table[@id='table_realStateAdministration']/tbody");
	By msgErrorUps = By.xpath("//*[@class='error__content text-center mt3']");

	By btnPagSiguiente = By.xpath("//a[contains(text(),'Siguiente')]/ancestor::li");
	By selectFormatoDescarga = By.xpath("//select[@id='exampleInputName2']");
	By selectFormatoDescarga2 = By.xpath("//select[@id='btnDownload']");

	By btnDescargar = By.xpath("//*[text()='Descargar']");
	By btnContinuar = By.xpath("//*[text()='Continuar']");
	By btnRegresar = By.xpath("//*[text()='Regresar']");
	By paginacionNumero = By.xpath("//li[@class='paginate_button active']/a");

	String opcionesDescarga = "//label[contains(normalize-space(),'{INFORME}')]/input";
	String linkDetallesInformes = "//table//td[contains(text(),'{PROYECTO}')]/ancestor::tr//input";
	By tablaInformes = By.xpath("//table[@id='reportPresale']/tbody");
	By tablaInformes2 = By.xpath("//table[@id='reportPresale']/tbody");
	By seleccionarInforme = By.xpath("//select[option[text()='Seleccione el informe']]");
	By ventanaDescarga = By
			.xpath("//*[@id='modalFileProcesing' and not(contains(@style,'display:disabled'))]//div[2]/p");

	By btnCerrar = By.xpath("//div[contains(@style,'display: block;')]//button");
	By btnAceptar = By
			.xpath("//*[@id='modalFileProcesing' and not(contains(@style,'display:disabled'))]//*[text()='Aceptar']");

	/*
	 * locators para la información requeria en los informes
	 */
	// Certificaciones
	By seleccionSubproyecto = By.xpath("//select[option[text()='Seleccione el subproyecto']]");
	By fechaProyecto = By
			.xpath("//div[@class='form-inline mt1 report__date' and not(contains(@style,'display: none'))]//input");
	By seleccionAnyo = By.xpath("//select[option[text()='Seleccione el año']]");
	By seleccionFormato = By.xpath("//select[option[text()='Seleccione el formato']]");
	By seleccionProducto = By.xpath("//select[option[text()='Seleccione el producto']]");
	By seleccionMes = By.xpath("//select[option[text()='Seleccione el mes']]");

	By fechaDesdeXp = By
			.xpath("//div[@class='form-inline mt1 report__date' and not(contains(@style,'display: none'))]//input[1]");
	By fechaHastaXp = By
			.xpath("//div[@class='form-inline mt1 report__date' and not(contains(@style,'display: none'))]//input[2]");

	By seleccionSemestre = By.xpath("//select[option[text()='Seleccione el semestre']]");

	// Descargar formatos
	By seleccioneLinea = By.xpath("//select[option[text()='Seleccione la línea']]");
	By textoFinal = By.xpath("//div[@class='modal-content']/div[2]/p");
	By btnAceptar2 = By.xpath("//*[text()='Aceptar']");
	String descargaFormato = "//tr/td[text()='{SELECCION_FORMATO}']/following-sibling::td";

	/**
	 * Realiza la descarga del documento [PDF] o [EXCEL] incluyendo los detalles, si
	 * sesolicitan
	 * 
	 * @param tipoPrueba
	 * @param formatoDescarga
	 * @param proyecto
	 * @return
	 * @throws Exception
	 */
	public String[] seccionFormatosYDetalles(String tipoPrueba, String formatoDescarga, String proyecto)
			throws Exception {
		this.pageOrigen = new PageOrigen(this);
		this.pageFiduciaFront = new PageFiduciaFront(this);

		this.pageOrigen.changeToFirstFrame();

		String[] msgError = new String[1];
		int contador = 0;
		WebElement selectFormatoDesc = null;
		WebElement tablaDatos = null;
		boolean datoEncontrado = false;
		msgError[0] = this.pageFiduciaFront.rutasFiducia(tipoPrueba);
		if (!msgError[0].isEmpty())
			return msgError;

		if (!msgError[0].isEmpty()) {
			return msgError;
		}
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
		do {
			contador++;
			msgError = new String[contador++];
			tablaDatos = this.element(PageFiduciaFront.labelSesionExiste);
		} while (tablaDatos == null && contador <= 1);

		if (tablaDatos != null) {
			msgError[0] = tablaDatos.getText();
//			Evidence.save(msgError[0] + " Evidencias", this);
			Evidence.save(msgError[0] + " Evidencias");
			return msgError;
		}
		contador = 0;
		tablaDatos = this.element(tableDetalles);

		if (tablaDatos == null) {
			DXCUtil.wait(1);
			tablaDatos = this.element(tableDetalles2);
		}
		List<WebElement> filas = tablaDatos.findElements(By.tagName("tr"));
		if (tipoPrueba.contains("Detalles")) {
			do {
				contador++;
				DXCUtil.wait(1);
				for (int i = 0; i < filas.size(); i++) {
					if (filas.get(i).getText().contains(proyecto)) {
						tablaDatos = this.element(By.xpath(linkDetalles.replace("{PROYECTO}", proyecto)));
						tablaDatos.click();
//						Evidence.save(tipoPrueba + " detalles Evidencias", this);
						Evidence.save(tipoPrueba + " detalles Evidencias");
						datoEncontrado = true;
						break;
					} else if (filas.get(i).getText().equals("No hay información disponible")) {
						msgError[0] = filas.get(i).getText();
						return msgError;
					}

					msgError[0] = this.pageFiduciaFront.msgError();

					if (msgError[0].contains("Sesión no existe")) {
						DXCUtil.wait(2);
//						Evidence.save(msgError[0] + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
						Evidence.save(msgError[0] + " Evidencias " + PageFiduciaFront.fechaHoraTx);
						return msgError;
					}
					if (datoEncontrado) {
						break;
					}
				}
				if (!datoEncontrado) {
					WebElement siguientePg = this.element(btnPagSiguiente);
					if (!siguientePg.getAttribute("class").contains("disabled")) {
						siguientePg.click();
//						Evidence.save(tipoPrueba + " detalles Evidencias", this);
						Evidence.save(tipoPrueba + " detalles Evidencias");
					} else {
						msgError[0] = "Se esperaba encontrar el Proyecto: [ " + proyecto + " ], pero no se encontro en "
								+ tipoPrueba;
//						Evidence.save(msgError[0] + " Evidencias", this);
						Evidence.save(msgError[0] + " Evidencias");
						return msgError;
					}
				}
			} while (!datoEncontrado);

			msgError[0] = this.pageFiduciaFront.msgError();

			if (!msgError[0].isEmpty()) {
//				Evidence.save(tipoPrueba + " detalles Evidencias", this);
				Evidence.save(tipoPrueba + " detalles Evidencias");
				return msgError;
			}

			contador = 0;
		}

		do {
			DXCUtil.wait(1);
			msgError[0] = this.pageFiduciaFront.msgError();
			if (!msgError[0].isEmpty())
				return msgError;
		} while (this.element(rutasFiducia) == null);
		Reporter.write("*** Ruta >>> " + this.element(rutasFiducia).getText());

		if (!tipoPrueba.contains("Detalles")) {
			if (filas.get(0).getText().contains("No hay información disponible")) {
				msgError[0] = filas.get(0).getText();
				return msgError;
			}
		}

		try {

			selectFormatoDesc = this.element(selectFormatoDescarga);
			if (selectFormatoDesc == null) {
				selectFormatoDesc = this.element(selectFormatoDescarga2);
			}

			this.focus(selectFormatoDesc);
			if (formatoDescarga.equalsIgnoreCase("Excel y Pdf")) {
				msgError = new String[2];
				for (int i = 0; i < msgError.length; i++) {
					String formato = "";
					if (i == 0) {
						formato = "Excel";
						msgError[0] = this.selectListItem(selectFormatoDesc, formato.toUpperCase());
					} else {
						formato = "Pdf";
						msgError[1] = this.selectListItem(selectFormatoDesc, formato.toUpperCase());
					}
//					Evidence.save(tipoPrueba + " detalles Evidencias", this);
					Evidence.save(tipoPrueba + " detalles Evidencias");
					if (msgError[0].isEmpty() || msgError[1].isEmpty())
						this.element(btnDescargar).click();

					msgError[i] = this.pageFiduciaFront.msgError();

					if (!msgError[i].contains("Sesión no existe") || msgError[i].isEmpty())
						msgError[i] = "Descargado el formato '" + formato + "' correctamente de "
								+ ControllerFiducia.getSubmenu(tipoPrueba)
								+ (tipoPrueba.contains("Detalles")
										? " Detalles >>> (Código de proyecto: '" + proyecto + "')"
										: "");
					else {
//						Evidence.save(msgError[0] + " Evidencias " + DXCUtil.hourToString("HH-mm"), this);
						Evidence.save(msgError[0] + " Evidencias " + DXCUtil.hourToString("HH-mm"));
						return msgError;
					}
				}

			} else {
				msgError[0] = this.selectListItem(selectFormatoDesc, formatoDescarga.toUpperCase());
//				Evidence.save(tipoPrueba + " detalles Evidencias", this);
				Evidence.save(tipoPrueba + " detalles Evidencias");
				DXCUtil.wait(1);
				if (msgError[0].isEmpty())
					this.element(btnDescargar).click();

				msgError[0] = this.pageFiduciaFront.msgError();
				if (msgError[0].isEmpty())
					msgError[0] = "Descargado el formato '" + formatoDescarga + "' correctamente de "
							+ ControllerFiducia.getSubmenu(tipoPrueba)
							+ (tipoPrueba.contains("Detalles")
									? " Detalles >>> (Código de proyecto: '" + proyecto + "')"
									: "");
				else {
//					Evidence.save(msgError[0] + " Evidencias", this);
					Evidence.save(msgError[0] + " Evidencias");
					return msgError;
				}
			}
//			Evidence.save(tipoPrueba + " detalles Evidencias", this);
			Evidence.save(tipoPrueba + " detalles Evidencias");
		} catch (Exception e) {
			msgError[0] = this.element(PageFiduciaFront.labelSesionExiste).getText();
			if (msgError[0] != null) {
				this.changeToDefaultFrame();
//				Evidence.save(msgError[0] + " Evidencias", this);
				Evidence.save(msgError[0] + " Evidencias");
				Reporter.write("*** No se esperaba el mensaje: '" + msgError[0]
						+ "', Se reintentara hacer la prueba nuevamente.");
				this.pageOrigen.refresh();
			}
			return msgError;
		}
		if (tipoPrueba.contains("Detalles")) {
//			Evidence.save(tipoPrueba + " detalles Evidencias", this);
			Evidence.save(tipoPrueba + " detalles Evidencias");
			this.element(btnRegresar);
		}

		return msgError;
	}

	/**
	 * Descargara el informe que se solicite en el caso de prueba, realizando la
	 * descarga o no
	 * 
	 * @param tipoPrueba
	 * @param formatoDescarga
	 * @param proyecto
	 * @param informacion
	 * @param informe
	 * @return
	 * @throws Exception
	 */
	public String[] seccionInformes(String tipoPrueba, String formatoDescarga, String proyecto, String informacion,
			String informe) throws Exception {

		this.pageOrigen = new PageOrigen(this);
		this.pageFiduciaFront = new PageFiduciaFront(this);

		this.pageOrigen.changeToFirstFrame();
		String confirmarDescarga = SettingsRun.getTestData().getParameter("Descargar Informe en la prueba");
		String[] msgError = new String[1];
		int contador = 0;
		WebElement selectFormatoInformacion = null;
		WebElement tablaDatos = null;
		WebElement popUpDescarga = null;
		boolean datoEncontrado = false;

		String subproyecto = SettingsRun.getTestData().getParameter("Seleccione subproyecto");
		String formato = SettingsRun.getTestData().getParameter("Formato");
		String fechaInforme = SettingsRun.getTestData().getParameter("Fecha informe");
		String selectAnyo = SettingsRun.getTestData().getParameter("Seleccione año");
		String selectProd = SettingsRun.getTestData().getParameter("Producto para seleccionar");
		String selectSemest = SettingsRun.getTestData().getParameter("Seleccione semestre");
		String fechaDesde = SettingsRun.getTestData().getParameter("Fecha desde");
		String fechaHasta = SettingsRun.getTestData().getParameter("Fecha hasta");
		String nInversiones = SettingsRun.getTestData().getParameter("No Inversiones");

		msgError[0] = this.pageFiduciaFront.rutasFiducia(tipoPrueba);
		if (!msgError[0].isEmpty())
			return msgError;
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
		do {
			contador++;
			msgError = new String[contador++];
			tablaDatos = this.element(PageFiduciaFront.labelSesionExiste);
		} while (tablaDatos == null && contador <= 1);

		if (tablaDatos != null) {
			msgError[0] = tablaDatos.getText();
//			Evidence.save(msgError[0] + " Evidencias", this);
			Evidence.save(msgError[0] + " Evidencias");
			return msgError;
		}

		contador = 0;
		tablaDatos = this.element(tablaInformes);
		List<WebElement> filas = tablaDatos.findElements(By.tagName("tr"));
		WebElement butContinuar = this.element(btnContinuar);
		if (!tipoPrueba.contains("Descarga de formatos") && !tipoPrueba.contains("Pendientes de descarga")) {
			do {
				contador++;
				DXCUtil.wait(1);
				for (int i = 0; i < filas.size(); i++) {
					if (filas.get(i).getText().contains(proyecto) && !proyecto.isEmpty()) {
						tablaDatos = this.element(By.xpath(linkDetallesInformes.replace("{PROYECTO}", proyecto)));
						tablaDatos.click();
//						Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
						Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
						datoEncontrado = true;
						break;
					} else if (filas.get(i).getText().equals("No hay información disponible")) {
						msgError[0] = filas.get(i).getText();
						return msgError;
					}

					msgError[0] = this.pageFiduciaFront.msgError();

					if (msgError[0].contains("Sesión no existe")) {
						DXCUtil.wait(2);
//						Evidence.save(msgError[0] + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
						Evidence.save(msgError[0] + " Evidencias " + PageFiduciaFront.fechaHoraTx);
						return msgError;
					}
					if (datoEncontrado) {
						break;
					}
				}
				if (!datoEncontrado) {
					WebElement siguientePg = this.element(btnPagSiguiente);
					if (!siguientePg.getAttribute("class").contains("disabled")) {
						siguientePg.click();
//						Evidence.save(tipoPrueba + " Evidencias", this);
						Evidence.save(tipoPrueba + " Evidencias");
					} else {
						msgError[0] = "Se esperaba encontrar el Proyecto: [ " + proyecto + " ], pero no se encontro en "
								+ tipoPrueba;
//						Evidence.save(msgError[0] + " Evidencias", this);
						Evidence.save(msgError[0] + " Evidencias");
						return msgError;
					}
				}
			} while (!datoEncontrado);
			contador = 0;

			msgError[0] = this.pageFiduciaFront.msgError();
			if (!msgError[0].isEmpty()) {
//				Evidence.save(tipoPrueba + " Evidencias", this);
				Evidence.save(tipoPrueba + " Evidencias");
				return msgError;
			}
			// Continua a la siguiente opción para descargar la información
			if (butContinuar != null && msgError[0].isEmpty())
				butContinuar.click();

			else {
				msgError[0] = "Se esperaba encontrar el botón: Continuar, pero no se encontro";
//				Evidence.save(msgError[0] + " Evidencias", this);
				Evidence.save(msgError[0] + " Evidencias");
				return msgError;
			}
//			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);

			msgError[0] = this.pageFiduciaFront.msgError();
			if (!msgError[0].isEmpty()) {
//				Evidence.save(tipoPrueba + " Evidencias", this);
				Evidence.save(tipoPrueba + " Evidencias");
				return msgError;
			}
		}

		try {
			selectFormatoInformacion = this.element(opcionesDescarga.replace("{INFORME}", informacion));
			if (selectFormatoInformacion != null) {
				selectFormatoInformacion.click();
//				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
				do {
					contador++;
					DXCUtil.wait(1);
				} while (this.element(seleccionarInforme) == null || contador <= 2);

				if (this.element(seleccionarInforme) != null) {
					DXCUtil.wait(2);
					String informeSlc = this.pageOrigen.selectListItem(seleccionarInforme, informe);
					if (!informeSlc.isEmpty()) {
						msgError[0] = informeSlc;
						return msgError;
					}
				} else {
					msgError[0] = this.pageFiduciaFront.msgError();
					return msgError;
				}
//				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
			} else {
				msgError[0] = "No se ha encontrado la opción: " + informacion;
//				Evidence.save(msgError[0] + " Evidencias", this);
				Evidence.save(msgError[0] + " Evidencias");
				return msgError;
			}
//			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
			msgError[0] = this.pageFiduciaFront.msgError();
			if (!msgError[0].isEmpty()) {
//				Evidence.save(msgError[0] + " Evidencias", this);
				Evidence.save(msgError[0] + " Evidencias");
				return msgError;
			}

			if (!subproyecto.equalsIgnoreCase("Seleccione una opción…")|| !formato.equalsIgnoreCase("Seleccione una opción…")) {
				switch (informacion) {
				case "Certificaciones":
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					if (this.element(seleccionSubproyecto) != null) {
						msgError[0] = this.pageOrigen.selectListItem(seleccionSubproyecto, subproyecto);
						if (!msgError[0].isEmpty())
							return msgError;
					}

					if (informe.equals("Certificación de ventas") || informe.equals("Certificación ICA")
							|| informe.equals("Certificación ICA"))
						this.pageOrigen.selectDatePicker(fechaProyecto, fechaInforme);
					else {
						msgError[0] = this.pageOrigen.selectListItem(seleccionAnyo, selectAnyo);
						if (!msgError[0].isEmpty())
							return msgError;
					}
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					break;

				case "Extractos":
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);

					msgError[0] = this.pageOrigen.selectListItem(seleccionAnyo, selectAnyo);
					if (!msgError[0].isEmpty())
						return msgError;

					if (this.element(seleccionSubproyecto) != null)
						msgError[0] = this.pageOrigen.selectListItem(seleccionSubproyecto, subproyecto);
					if (!msgError[0].isEmpty())
						return msgError;

					String datoCta = DXCUtil.right(selectProd, 4);
					msgError[0] = this.pageOrigen.selectListItem(seleccionProducto, datoCta);
					if (!msgError[0].isEmpty())
						return msgError;

					msgError[0] = this.pageOrigen.selectListItem(seleccionMes, selectAnyo);
					if (!msgError[0].isEmpty())
						return msgError;
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					break;

				case "Informes Administrativos":
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					if (tipoPrueba.contains("Informes Preventa")) {
						msgError[0] = this.pageOrigen.selectListItem(seleccionAnyo, selectAnyo);
						if (!msgError[0].isEmpty())
							return msgError;

						msgError[0] = this.pageOrigen.selectListItem(seleccionSemestre, selectSemest);
						if (!msgError[0].isEmpty())
							return msgError;
					} else {
						if (this.element(seleccionMes) != null) {
							msgError[0] = this.pageOrigen.selectListItem(seleccionMes, selectAnyo);
							if (!msgError[0].isEmpty())
								return msgError;
						} else {
							if (this.element(fechaProyecto) != null) {
								this.pageOrigen.selectDatePicker(fechaProyecto, fechaInforme);
							}

						}
					}
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					break;

				case "Informes Operativos":
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					if (this.element(seleccionProducto) != null)
						msgError[0] = this.pageOrigen.selectListItem(seleccionProducto, nInversiones);
					if (!msgError[0].isEmpty())
						return msgError;

					if (this.element(fechaDesdeXp) != null && this.element(fechaHastaXp) != null) {
						this.pageOrigen.selectDatePicker(fechaDesdeXp, fechaDesde);
						this.pageOrigen.selectDatePicker(fechaHastaXp, fechaHasta);
					} else {
						this.pageOrigen.selectDatePicker(fechaProyecto, fechaInforme);
					}
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					break;

				case "Movimientos":
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					WebElement weFechaDesde = this.element(fechaDesdeXp);
					WebElement weFechaHasta = this.element(fechaHastaXp);
					if (weFechaDesde != null && weFechaHasta != null) {
						this.pageOrigen.selectDatePicker(weFechaDesde, fechaDesde);
						this.pageOrigen.selectDatePicker(weFechaHasta, fechaHasta);
					} else {
						this.pageOrigen.selectDatePicker(fechaProyecto, fechaInforme);
					}
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					break;

				case "Reportes financieros":
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					if (this.element(fechaDesdeXp) != null && this.element(fechaHastaXp) != null) {
						this.pageOrigen.selectDatePicker(fechaDesdeXp, fechaDesde);
						this.pageOrigen.selectDatePicker(fechaHastaXp, fechaHasta);
					} else if (this.element(fechaProyecto) != null) {
						this.pageOrigen.selectDatePicker(fechaProyecto, fechaInforme);
					} else {
						msgError[0] = this.pageOrigen.selectListItem(seleccionAnyo, selectAnyo);
						if (!msgError[0].isEmpty())
							return msgError;
					}
//					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
					break;
				}

				// selecciona el formato final y descargara
				this.focus(seleccionFormato);
//				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
				msgError[0] = this.pageOrigen.selectListItem(seleccionFormato, formato);
				if (!msgError[0].isEmpty())
					return msgError;

				this.element(btnDescargar).click();
//				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
				msgError[0] = this.pageFiduciaFront.msgError();
				if (!msgError[0].isEmpty()) {
					Evidence.save(msgError[0] + " Evidencias");
					return msgError;
				}

			} else {
				msgError[0] = "Error data campo/s con [Seleccione una opción…]";
				return msgError;
			}
			contador = 0;
			do {
				DXCUtil.wait(1);
				contador++;
				popUpDescarga = this.element(ventanaDescarga);
			} while (popUpDescarga == null && contador <= 2);
//			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
			msgError[0] = popUpDescarga.getText();
			if (confirmarDescarga.contains("Seleccione una opción")) {
				msgError[0] = "Error data campo con [Seleccione una opción…]";
				return msgError;
			}

			if (confirmarDescarga.contains("SI")) {
				this.element(btnAceptar).click();
//				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
				msgError[0] = this.pageFiduciaFront.msgError();
				if (!msgError[0].isEmpty()) {
					Evidence.save(msgError[0] + " Evidencias");
					return msgError;
				}

				String descargaPendiente = this.descargasPendientes();
				if (descargaPendiente.isEmpty()) {
					msgError[0] = "Se ha descargado el archivo correctamente";
				} else {
					msgError[0] = descargaPendiente;
				}

			} else {
				this.element(btnCerrar).click();
//				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
				Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
				return msgError;
			}

		} catch (Exception e) {

			msgError[0] = this.element(PageFiduciaFront.labelSesionExiste).getText();
			if (msgError[0] != null) {
				this.changeToDefaultFrame();
				Evidence.save(msgError[0] + " Evidencias");
				Reporter.write("*** No se esperaba el mensaje: '" + msgError[0]
						+ "', Se reintentara hacer la prueba nuevamente.");
				this.pageOrigen.refresh();
			}
			return msgError;
		}
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
		return msgError;
	}

	/**
	 * Descarga los formatos, que se pueden descargar desde informes
	 * 
	 * @param tipoPrueba
	 * @return
	 * @throws Exception
	 */
	public String[] descargaFormatos(String tipoPrueba) throws Exception {
		String lineaConsultar = SettingsRun.getTestData().getParameter("Línea a consultar").trim();
		String linea = SettingsRun.getTestData().getParameter("Línea").trim();
		String formatoLinea = SettingsRun.getTestData().getParameter("Formato de línea").trim();
		String[] msgError = new String[1];
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);

		this.pageOrigen = new PageOrigen(this);
		this.pageFiduciaFront = new PageFiduciaFront(this);

		this.pageOrigen.changeToFirstFrame();

//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
		msgError[0] = this.pageFiduciaFront.rutasFiducia(tipoPrueba);
		if (!msgError[0].isEmpty())
			return msgError;
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);

		msgError[0] = this.pageFiduciaFront.msgError();
		if (!msgError[0].isEmpty()) {
			Evidence.save(msgError[0] + " Evidencias");
			return msgError;
		}

		WebElement lineaConsulta = this.element(By.xpath(opcionesDescarga.replace("{INFORME}", lineaConsultar)));
		if (lineaConsulta != null)
			lineaConsulta.click();

//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);

		msgError[0] = this.pageFiduciaFront.msgError();
		if (!msgError[0].isEmpty()) {
			Evidence.save(msgError[0] + " Evidencias");
			return msgError;
		}

		msgError[0] = this.selectListItem(seleccioneLinea, linea);
		if (!msgError[0].isEmpty())
			return msgError;

		msgError[0] = this.pageFiduciaFront.msgError();
		if (!msgError[0].isEmpty()) {
			Evidence.save(msgError[0] + " Evidencias");
			return msgError;
		}
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);

		WebElement formatoDescargar = this
				.element(By.xpath(descargaFormato.replace("{SELECCION_FORMATO}", formatoLinea)));

		if (formatoDescargar != null) {
			DXCUtil.wait(1);
			formatoDescargar.click();

			msgError[0] = this.pageFiduciaFront.msgError();
			if (!msgError[0].isEmpty()) {
				Evidence.save(msgError[0] + " Evidencias");
				return msgError;
			}

			msgError[0] = "Formato Descargado correctamente";
//			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
		}
//		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save(tipoPrueba + " Evidencias " + PageFiduciaFront.fechaHoraTx);
		return msgError;
	}

	/**
	 * Descarga el documento solicitado de descargas pendientes, metodo se usara
	 * para hacer una descarga inmediata
	 * 
	 * @return
	 * @throws Exception
	 */

	private String descargasPendientes() throws Exception {
		WebElement ultimaFila = null;
		WebElement celdaDescarga = null;
		WebElement linkDescarga = null;
		String msgError = "";

		WebElement tabla = this.element("//*[@id='table_reportsPending']");
		while (true) {

			WebElement btnSiguientePgn = this.element(btnPagSiguiente);
			if (btnSiguientePgn.getAttribute("class").contains("disabled")) {
				break;
			}
			btnSiguientePgn.click();

//			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);

			msgError = this.pageFiduciaFront.msgError();
			if (!msgError.isEmpty()) {
				Evidence.save(msgError + " Evidencias");
				return msgError;
			}

		}
//		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
		if (tabla != null) {
			List<WebElement> filas = tabla.findElements(By.tagName("tr"));
			ultimaFila = filas.get(filas.size() - 1);

			celdaDescarga = ultimaFila.findElement(By.xpath("td[8]"));
//			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
		}

		try {
			linkDescarga = celdaDescarga.findElement(By.tagName("a"));
		} catch (Exception e) {
		}

		if (linkDescarga != null) {
			linkDescarga.click();
//			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);

			do {
				DXCUtil.wait(1);
			} while (this.element(textoFinal) == null);

			if (this.element(textoFinal) != null) {
				this.element(btnAceptar2).click();
			}
			msgError = this.pageFiduciaFront.msgError();
			if (!msgError.isEmpty()) {
				Evidence.save(msgError + " Evidencias");
				return msgError;
			}

		} else {
			String msgEncontrado = celdaDescarga.getText();
			this.focus(celdaDescarga);
//			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
			return msgEncontrado;
		}

//		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
		return this.element(textoFinal).getText();
	}

	/**
	 * Realizara la descarga de todos los archivos que tengan en relación la misma
	 * fecha de solicitud, unicamente se realizara por la fecha actual
	 * 
	 * @param tipoPrueba
	 * @return
	 * @throws Exception
	 */
	public String[] descargarDocumentosPendientes(String tipoPrueba) throws Exception {
		String[] msgError = new String[1];
		WebElement celdaDescarga = null;
		WebElement linkDescarga = null;
		String informacionDocumentos = null;
		String[] informacion = null;
		boolean totalPaginas = false;

		this.pageOrigen = new PageOrigen(this);
		this.pageFiduciaFront = new PageFiduciaFront(this);

		this.pageOrigen.changeToFirstFrame();

//		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
		String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		WebElement btnSiguientePgn = this.element(btnPagSiguiente);

		msgError[0] = this.pageFiduciaFront.rutasFiducia(tipoPrueba);
		if (!msgError[0].isEmpty())
			return msgError;

		do {
			WebElement tabla = this.element("//*[@id='table_reportsPending']");
			List<WebElement> filas = tabla.findElements(By.tagName("tr"));

			for (int i = 0; i < filas.size(); i++) {
				if (filas.get(i).getText().contains(fechaActual)) {
					celdaDescarga = filas.get(i).findElement(By.xpath("td[8]"));
					try {
						linkDescarga = celdaDescarga.findElement(By.tagName("a"));
					} catch (Exception e) {
					}

//					Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
					Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
					if (linkDescarga != null) {
						this.focus(linkDescarga);
						linkDescarga.click();

						msgError[0] = this.pageFiduciaFront.msgError();
						if (!msgError[0].isEmpty()) {
							Evidence.save(msgError + " Evidencias");
							return msgError;
						}

						informacionDocumentos += "Se ha descargado correctamente el ducumento|";

					} else {
						informacionDocumentos += celdaDescarga.getText() + "|";
					}
				}
			}

			if (informacionDocumentos == null) {
				msgError[0] = "No se encontro información con fecha relacionada.";
				return msgError;
			}
			
//			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
			Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
			if (!btnSiguientePgn.getAttribute("class").contains("disabled")) {
				btnSiguientePgn.click();

				msgError[0] = this.pageFiduciaFront.msgError();
				if (!msgError[0].isEmpty()) {
					Evidence.save(msgError[0] + " Evidencias");
					return msgError;
				}
			} else {
				totalPaginas = true;
			}
		} while (!totalPaginas);

		informacion = informacionDocumentos.split("\\|");

//		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx, this);
		Evidence.save("Evidencias " + PageFiduciaFront.fechaHoraTx);
		return informacion;
	}

}
