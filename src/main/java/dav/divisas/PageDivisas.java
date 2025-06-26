package dav.divisas;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dav.pymes.PagePortalPymes;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class PageDivisas extends PagePortalPymes {

	public PageDivisas(BasePageWeb parentPage) {
		super(parentPage);
	}

// =======================================================================================================================
	public By iframeIdDivisas = By.id("cphCuerpo_IframeDivisas");

	By sesionEx = By.xpath("//b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]");

	// Locator MENU
	String moduloSelecionarDivisas = "//li[contains(text(), 'MENU')]";

	// Locator [ALERTAS-POPUP-MENSAJES]
	By popMenAler = By.xpath("//p[contains(text(), 'disponible')]");
	By btnMenAlerdisponible = By.id("btnmodal");
	By popMens = By.xpath("//div[@id='AlertaModal']//strong[contains(text(), 'Importante')]");

	By cmpPopup = By.id("mensaje");
	By btnPopup = By.xpath("//*[@id='botonModal' or @id='alertamodalbtn']");
	By btnPopupaceptar = By.id("btnmodalaceptar");
	By btnPopupAvertaceptar = By.xpath("//*[@id='AlertaModal']/div//button[contains(text(), 'Aceptar')]");
	By btnModalPopup = By.id("btnmodal2");
	By btnPopup2 = By.xpath("//*[@id='btnmodal']");
	By btnAcetarAler = By.id("alertamodalbtn");

	By AlertPopup = By.xpath("//*[@id='AlertaModal']/div");
	By AlertPopupAdver = By.xpath("//*[@id='AlertaModalComponente']/div");

	String Alermod = "//*[@id='AlertaModal']/div/div/div[2]/p";
	String Alermod2 = "//*[@id='AlertaModal']/div";

// =======================================================================================================================

	// Busca el registro mediante la hora y el tipo de moneda
	String xpathNumDocumTxCon = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/following-sibling::td[contains(text(), 'DOCID')]/preceding-sibling::td/a";
	String xpathNumDocumTxCon2 = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/preceding-sibling::td[4]";

	String xpathNumDocumTxCon3 = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/preceding-sibling::td[3]";

	String xpathNumDocumTxObt = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/following-sibling::td[contains(text(), '')][3]";

	String xpathBuscarFechayHora = "//td[contains(text(), 'fechayhoraconvert')]";

// =======================================================================================================================

	static String numAprova = null;

	private static int contador = 1;

	/**
	 * Llama el dato de numero de aprobacion desde la transaccion
	 */
	public void SetNumAprInterna(String numAproTx) {
		numAprova = numAproTx;
	}

// =======================================================================================================================

	/**
	 * Espera hasta que el iframe de divisas esté disponible, lo selecciona y hace
	 * zoom.
	 * 
	 * @return true si se cargó correctamente, false si hubo timeout.
	 * @throws Exception
	 */
	public boolean switchToFrameDivisas() throws Exception {
		int contador = 0;

		while (contador < 30) {
			DXCUtil.wait(1);
			WebElement iframe = this.element(iframeIdDivisas);
			if (iframe != null) {
				this.getDriver().switchTo().frame(iframe);
				this.getJse().executeScript("document.body.style.zoom ='90%';");
				return true;
			}
			contador++;

			if (this.element(sesionEx) != null) {
				String msg = this.element(sesionEx).getText();
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			}
			this.getDriver().switchTo().defaultContent();
		}
		this.getDriver().switchTo().defaultContent();
		Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut: No se presentó el módulo de divisas");
		return false;
	}

// =======================================================================================================================

	/**
	 * Se en carga de selecionar el modulo de Divisas: [Recibir dinero del exterior
	 * - Enviar Transferencias Internacionales - Aprobaciones - Documentos y
	 * Formularios - Consultas]
	 * 
	 * @param servicio: [Recibir dinero del exterior - Enviar Transferencias
	 *                  Internacionales - Aprobaciones - Documentos y Formularios -
	 *                  Consultas]
	 * @return
	 * @throws Exception
	 */
	public String seleccionarTransferencia(String servicio) throws Exception {

		this.ErrorSesionExpirada();

		Evidence.save("Transferencias internacionales");

		String msgError = this.getMsgAlertIfExist("lblMasterAlerta");
		if (isValid(msgError)) {
			return msgError;
		}

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				// Divisas
				this.getDriver().switchTo().defaultContent();
				Evidence.save("No se encuentra el modulo: " + servicio);
				return "No se encuentra el modulo: " + servicio;
			}

			if (this.element(popMenAler) != null) {
				DXCUtil.wait(5);

				String msAlertam = this.getText(popMenAler);
				Reporter.reportEvent(Reporter.MIC_FAIL, msAlertam);
				this.getDriver().switchTo().defaultContent();
				Evidence.save(msAlertam);
				return msAlertam;
			}

			msgError = this.getMsgAlertIfExist("lblMasterAlerta");
			if (isValid(msgError)) {
				return msgError;
			}

		} while (this.element(moduloSelecionarDivisas.replace("MENU", servicio)) == null);

		this.click(By.xpath(moduloSelecionarDivisas.replace("MENU", servicio)));

		this.ErrorSesionExpirada();

		msgError = this.getMsgAlertIfExist("lblMasterAlerta");

		if (isValid(msgError)) {
			return msgError;
		}

		return "";
	}

// =======================================================================================================================

	public String obtenerNumeroTxDocumentoGeneral(String page) throws Exception {
		String documentoTx = null;
		String numAprovaLocal = numAprova;

		// 1. Intentar por número de aprobación
		if (!isValid(numAprovaLocal)) {
			numAprovaLocal = SettingsRun.getTestData().getParameter("Número Aprobación");
		}

		if (isValid(numAprovaLocal)) {

			documentoTx = xpathNumDocumTxCon.replace("fechayhoraconvert", "").replace("MONEDA", "").replace("DOCID",numAprovaLocal);

			if (this.element(documentoTx) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "El numero de Aprobacion no se encontro: " + numAprova);
				documentoTx = null;
				
			}else {
				
				String objeto =  this.element(documentoTx).getText();
				if (objeto.equals("Editar")) {
					this.element(documentoTx).click();
					return numAprovaLocal;
					
				}else {
					return documentoTx;
				}
				
			}
			
			if (isValid(documentoTx))
				return documentoTx;
		}

		// 2. Intentar por fecha y hora (con varios intentos de ajuste)

		String fecha = SettingsRun.getTestData().getParameter("Fecha tx");
		String hora = SettingsRun.getTestData().getParameter("Hora tx");

		if (isValid(fecha) && isValid(hora)) {

			// Buscar botón "Siguiente"
			documentoTx = SiguientePagina(fecha, hora, page);

			String horaIntento = hora;
			horaIntento = DXCUtil.horaAdd("HH:mm", hora, 1);

			documentoTx = findDocumentWithTime(fecha, horaIntento, page);

			if (isValid(documentoTx))
				return documentoTx;

			for (int minutosARestar = 0; minutosARestar <= 5; minutosARestar++) {
				horaIntento = hora;
				for (int i = 0; i < minutosARestar; i++)
					horaIntento = DXCUtil.subtractOneMinute(horaIntento);
				
				
				documentoTx = findDocumentWithTime(horaIntento);
				
				if (!isValid(documentoTx)) {
					
				documentoTx = findDocumentWithTime(fecha, horaIntento, page);
				}

				if (isValid(documentoTx))
					return documentoTx;
			}

			Reporter.reportEvent(Reporter.MIC_FAIL, "Error: Documento no encontrado con tiempos ajustados.");
		}

		return documentoTx;
	}



	public String SiguientePagina(String fecha, String hora, String page) throws Exception {
		
		String servicio = SettingsRun.getTestData().getParameter("Servicio");
		// Buscar botón "Siguiente"
		WebElement btnSiguiente = null;

		String documentoTx = null;

		btnSiguiente = this.element(By.xpath("//button[contains(text(),'Siguiente')]"));

		if (isElementInteractable(btnSiguiente)) {

			WebElement paginaElement = this.element(By.xpath("//*[@id='pagina']"));

			// Obtiene la opción seleccionada
			String paginaActualStr = null;
			int paginaActual = 0;
			int totalPaginas = 0;
			Select dropdown = null;
			String[] paginas = null;

			// Crea un objeto Select para manipularlo
			if (paginaElement != null) {
				dropdown = new Select(paginaElement);
				paginaActualStr = dropdown.getFirstSelectedOption().getText().trim();
			}

			if (isValid(paginaActualStr)) {
				paginas = paginaActualStr.split("de");
				paginaActual = Integer.parseInt(paginas[0].trim());
				totalPaginas = Integer.parseInt(paginas[1].trim());
			}

			do {

				btnSiguiente = this.element(By.xpath("//button[contains(text(),'Siguiente')]"));

				// Buscar la fecha y hora en la página actual
				List<WebElement> listaFecha = null;
				List<WebElement> listahora = null;

				if (isValid(fecha))
					listaFecha = this.findElements(By.xpath(xpathBuscarFechayHora.replace("fechayhoraconvert", fecha)));

				String horaconver = null;

				String[] horaes = hora.split(":");
				
				if (isValid(hora) && !horaes[0].equals("12") && !servicio.equals("Consulta Tx Internacionales Validar Estado"))
					horaconver = DXCUtil.convertirHoraSiPM(hora);

				else
					
					horaconver = hora;

				listahora = this.findElements(By.xpath(xpathBuscarFechayHora.replace("fechayhoraconvert", horaconver)));

				
				// Si encuentra ambos, intenta obtener el documento
				if (listaFecha != null && listahora != null) {
					
					documentoTx = findDocumentWithTime(hora);
					
					if (!isValid(documentoTx)) {
						
					documentoTx = findDocumentWithTimeAfterDelay(fecha, hora, page);
					}
					if (isValid(documentoTx)) {
						// Documento encontrado
						return documentoTx;
					}
				}

				paginaElement = this.element(By.xpath("//*[@id='pagina']"));
				// Crea un objeto Select para manipularlo
				dropdown = new Select(paginaElement);

				// Obtiene la opción seleccionada
				paginaActualStr = dropdown.getFirstSelectedOption().getText().trim();
				paginas = paginaActualStr.split("de");

				paginaActual = Integer.parseInt(paginas[0].trim());

				// Verificamos si ya estamos en la última página
				if (paginaActual >= totalPaginas) {
					return null;
				}

				// Verificar si el botón siguiente está disponible
				btnSiguiente = this.element(By.xpath("//button[contains(text(),'Siguiente')]"));

				if (isElementInteractable(btnSiguiente)) {
					this.click(btnSiguiente);
					DXCUtil.wait(10); // Espera a que cargue la siguiente página
				} else {
					return null;
				}

			} while (!isElementInteractable(btnSiguiente));
		}

		return documentoTx;

	}


	// ============================================[findDocumentWithTimeAfterDelay]===========================================================================

	public String findDocumentWithTimeAfterDelay(String fecha, String horaconvert, String page) throws Exception {
		// Convierte minutos a milisegundos
		return findDocumentWithTime(fecha, horaconvert, page);
	}

	// ============================================[findDocumentWithTime]===========================================================================

	/**
	 * Obtiene el número de transacción o documento de la transacción según fecha y
	 * hora.
	 *
	 * @param fecha       Fecha de la transacción en formato esperado (ej.
	 *                    "yyyy-MM-dd")
	 * @param horaConvert Hora de la transacción en formato esperado (ej. "HH:mm")
	 * @return Número de transacción o documento, o null si ocurre un error
	 * @throws Exception en caso de error en el procesamiento
	 */
	public String findDocumentWithTime(String fecha, String horaconvert, String page) throws Exception {

		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
		String servicio = SettingsRun.getTestData().getParameter("Servicio");
		String pendieteAprobar = SettingsRun.getTestData().getParameter("Tipo prueba");

		WebElement obTNumDocumTxCon = null;
		try {

			String fechayHoraconver = null;

			if (servicio.equals("Consulta Tx Internacionales Enviar al exterior Validar Estado") && !pendieteAprobar.contains("Pend")) {

				fechayHoraconver = fecha + " " + horaconvert;

				obTNumDocumTxCon = this.element(xpathNumDocumTxCon2.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda));
				Reporter.write(fechayHoraconver);

				if (obTNumDocumTxCon != null) {
					return obTNumDocumTxCon.getText();
				}

				if (isValid(horaconvert) && !horaconvert.equals("12") && !servicio.equals("Consulta Tx Internacionales Validar Estado"))
					fechayHoraconver = fecha + " " + DXCUtil.convertirHoraSiPM(horaconvert);

				obTNumDocumTxCon = this.element(
						xpathNumDocumTxCon2.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda));
				Reporter.write(fechayHoraconver);

				if (obTNumDocumTxCon != null) {
					return obTNumDocumTxCon.getText();
				}

			}

			if (!servicio.equals("Consulta Tx Internacionales Enviar al exterior Validar Estado")&& pendieteAprobar.contains("Pend") && page.equals("Aprobaciones")) {

				obTNumDocumTxCon = this.element(xpathNumDocumTxCon3.replace("fechayhoraconvert", horaconvert).replace("MONEDA", moneda));
				Reporter.write(horaconvert);

				if (obTNumDocumTxCon != null) {
					return obTNumDocumTxCon.getText();
				}

			} else {

				fechayHoraconver = horaconvert;
				if (isValid(horaconvert)&&!servicio.equals("Consulta Tx Internacionales Validar Estado"))
				fechayHoraconver = DXCUtil.convertirHoraSiPM(fechayHoraconver);
				Reporter.write(fechayHoraconver);

				obTNumDocumTxCon = this.element(xpathNumDocumTxCon2.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda));

				if (obTNumDocumTxCon != null) {
					return obTNumDocumTxCon.getText();
				}

				String horaConver = null;

				String[] fechayHoraSepara = fechayHoraconver.split(" ");

				String[] horaSepara = fechayHoraSepara[1].split(":");

				if (isValid(horaconvert) && !horaSepara[0].equals("12"))

					fechayHoraconver = fecha + " " + DXCUtil.convertirHoraSiPM(fechayHoraSepara[1]);

				else

					fechayHoraconver = fecha + " " + horaConver;

				Reporter.write(fechayHoraconver);

				obTNumDocumTxCon = this.element(
						xpathNumDocumTxCon2.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda));

				if (obTNumDocumTxCon != null) {
					return obTNumDocumTxCon.getText();
				}
			}

		} catch (Exception e) {

			return null;
		}

		return null;
	}

	/**
	 * Este metodo Obtiene el [Número de tx o Documento de la Tx] con la fecha y
	 * Hora si son necesarios
	 * 
	 * @param fecha       Tx
	 * @param horaconvert Tx
	 * @return Retorna el [Número de tx o Documento de la Tx] o null
	 * @throws Exception
	 */
	public String findDocumentWithTime(String horaconvert) throws Exception {

		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
		String servicio = SettingsRun.getTestData().getParameter("Servicio");
		
		String documentoTx = "";
		String fechayHoraconver = "";

		if (isValid(numAprova))
			documentoTx = numAprova;

		String[] horaes = horaconvert.split(":");
		if (isValid(horaconvert) && !horaes[0].equals("12") && !servicio.equals("Consulta Tx Internacionales Validar Estado"))
			fechayHoraconver = DXCUtil.convertirHoraSiPM(horaconvert);

		else
			fechayHoraconver = horaconvert;

		try {
			DXCUtil.wait(1);

			String Obje = xpathNumDocumTxCon.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda).replace("DOCID", documentoTx);

			WebElement ObjetodocumentoTx =null;
			
			ObjetodocumentoTx = this.element(Obje);
			if (ObjetodocumentoTx == null)
				 Obje = xpathNumDocumTxObt.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda);
			
		     	ObjetodocumentoTx = this.element(Obje);
			if (!isElementPresentAndNotInteractable(ObjetodocumentoTx))
				documentoTx = ObjetodocumentoTx.getText();
			if (isValid(documentoTx)&&!documentoTx.contains(".")&&!documentoTx.contains(",")) {
				
			numAprova = documentoTx;
			return documentoTx ;
			}else {
				return null;
			}
			
		} catch (Exception e) {

			return null;
		}

	}

// =========================================================================================================================================

	/**
	 * Metodo que retorna el mensaje de alerta si este existe en divisas. Si el
	 * retorno es [null] es porque NO existe un mensaje de alerta.<br>
	 * 
	 * @return Mensaje Alerta del portal
	 * @throws Exception
	 */
	public String closeActiveIntAlert() throws Exception {

		int contador = 1;

		String msgResp = null;

		if (isElementInteractable(btnModalPopup)) {

			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(btnModalPopup) && contador <= 7);

			msgResp = this.getMsgAlertIfExist("AlertaModal");

		} else if (isElementInteractable(btnPopup)) {

			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(btnPopup) && contador <= 7);

			msgResp = this.getMsgAlertIfExist("mensaje");
		} else if (isElementInteractable(AlertPopupAdver)) {
			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(AlertPopupAdver) && contador <= 7);

			msgResp = this.getMsgAlertIfExist("mensaje");
		} else {
			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(btnPopup2) && contador <= 7);

			msgResp = this.getMsgAlertIfExistxPath(Alermod);
		}

		if (isElementInteractable(Alermod2)) {

			do {
				DXCUtil.wait(6);
				contador++;
			} while (isElementPresentAndNotInteractable(Alermod2) && contador <= 7);
			msgResp = this.getMsgAlertIfExistxPath(Alermod2);// cambiar el extraer

		}

		if (isElementInteractable(btnModalPopup)) {
			Evidence.saveAllScreens("Alert", this);
			this.click(btnModalPopup);
		} else if (isElementInteractable(btnPopup2)) {
			Evidence.saveAllScreens("Alert", this);
			this.click(btnPopup2);
		} else if (isElementInteractable(btnPopup)) {
			Evidence.saveAllScreens("Alert", this);
			this.click(btnPopup);
		} else if (isElementInteractable(AlertPopupAdver)) {
			Evidence.saveAllScreens("Alert", this);
			this.click(btnAcetarAler);
		} else if (isElementInteractable(Alermod2)) {
			Evidence.saveAllScreens("Alert", this);
			if (isElementInteractable(btnPopupaceptar))
				this.click(btnPopupaceptar);
			if (isElementInteractable(btnPopupAvertaceptar))
				this.click(btnPopupAvertaceptar);
		}

		return msgResp;
	}

	/**
	 * Retorna el mensaje de alerta si existe alguna alerta activa en divisas, sin
	 * cerrar el popup. Realiza la gestión adecuada de los diferentes tipos de
	 * popups presentes en la página y guarda evidencia si corresponde.
	 *
	 * @return Mensaje de alerta mostrado o null si no existe alerta activa.
	 * @throws Exception Si ocurre un error durante la interacción.
	 */
	public String getActiveIntAlert() throws Exception {
		int contador = 1;
		String msgResp = null;

		if (isElementInteractable(btnModalPopup)) {
			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(btnModalPopup) && contador <= 10);

			msgResp = this.getMsgAlertIfExist("AlertaModal"); // cambiar el extraer mensaje popup

		} else if (isElementInteractable(btnPopup)) {
			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(btnPopup) && contador <= 10);

			msgResp = this.getMsgAlertIfExist("mensaje"); // cambiar el extraer mensaje popup

		} else if (isElementInteractable(AlertPopupAdver)) {
			DXCUtil.wait(1);
			msgResp = this.element(AlertPopupAdver).getAttribute("outerText");

		} else {
			do {
				DXCUtil.wait(1);
				contador++;
			} while (isElementPresentAndNotInteractable(btnPopup2) && contador <= 10);

			msgResp = this.getMsgAlertIfExistxPath(Alermod); // cambiar el extraer mensaje popup
		}

		// Guardar evidencia visual de la alerta detectada
		if (isElementInteractable(btnModalPopup)) {
			Evidence.saveAllScreens("Alert", this);
		} else if (isElementInteractable(btnPopup2)) {
			Evidence.saveAllScreens("Alert", this);
		} else if (isElementInteractable(btnPopup) && isElementInteractable(cmpPopup)) {
			Evidence.saveAllScreens("Alert", this);
		} else if (isElementInteractable(AlertPopupAdver)) {
			Evidence.saveAllScreens("Alert", this);
		}

		return msgResp;
	}

	// =========================================================================================================================================

	/**
	 * Cierra el mensaje alerta (popup) que requiere confirmación en la sección de
	 * divisas. Gestiona diferentes tipos de popups, guarda evidencia y retorna el
	 * mensaje de alerta mostrado.
	 *
	 * @return Mensaje de alerta mostrado, o "TimeOut" si no se puede cerrar la
	 *         alerta dentro del tiempo máximo.
	 * @throws Exception Si ocurre un error durante la interacción con los popups.
	 */
	public String closeActiveIntAlertConfirma() throws Exception {
		int contador = 1;
		String msgResp = null;

		// Esperar a que el popup esté disponible o expire el tiempo máximo de espera
		do {
			DXCUtil.wait(1);
			contador++;

			if (this.element(sesionEx) != null) {
				String msg = this.element(sesionEx).getText();
				Evidence.save("msg");
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			}
//	            return "TimeOut";

			if (isElementInteractable(cmpPopup)) {
				Evidence.save("AlertPopup");
			}

		} while (isElementPresentAndNotInteractable(btnPopup) && contador <= 7);

		// Obtener el mensaje de alerta si existe en los diferentes posibles popups
		if (this.element("//*[@id='mensajeerror']") != null) {

			msgResp = this.getMsgAlertIfExist("mensajeerror");// cambiar el extraer mensaje popup

		} else if (this.element("//*[@id='mensaje']") != null) {

			msgResp = this.getMsgAlertIfExist("mensaje");// cambiar el extraer mensaje popup

		} else if (this.element(Alermod) != null) {

			msgResp = this.getMsgAlertIfExistxPath(Alermod); // cambiar el extraer mensaje popup
		}

		// Cerrar el popup y guardar evidencia
		if (isElementInteractable(btnPopup)) {

			if (this.isDisplayed(btnPopup)) {
				Evidence.save("MensajeAlert");
				this.click(btnPopup);
			}

		} else if (isElementInteractable(btnPopup)) {

			if (this.element(Alermod) != null) {

				msgResp = this.getMsgAlertIfExistxPath(Alermod);// cambiar el extraer mensaje popup
			}

			if (this.isDisplayed(btnPopup)) {

				Evidence.save("MensajeAlert");
				this.click(btnPopup);

			}
		}

		return msgResp;
	}

// =========================================================================================================================================

	/**
	 * Validad que si aparcese el mensaje de Sesión no existe o ha expirado cierra
	 * sesión.
	 * 
	 * @throws Exception
	 */
	public String ErrorSesionExpirada() throws Exception {
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (this.element(sesionEx) != null) {

				Evidence.save("Sesión no existe o ha expirado");
				this.getDriver().switchTo().defaultContent();
				return "Sesión no _Existe o ha expirado";
			}
		} while (contador <= 5);

		return null;
	}

// =========================================================================================================================================

	/**
	 * Si aparece el mensaje de sesion expirada cierra sesion, pasa al siguiente row
	 */
	public String waitForElementToAppear(String elementLocator) throws Exception {
		contador = 0;
		String msg = null;
		do {
			DXCUtil.wait(1);
			if (contador >= 30) {
				if (this.element(sesionEx) != null) {
					msg = this.element(sesionEx).getText();
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

				return "TimeOut No se presentó el campo";
			}
		} while (this.element(elementLocator) == null);
		return msg;
	}

// =========================================================================================================================================

	public String assertElementExists(By elementLocator, String successMessage) throws Exception {
		String msg = null;

		if (this.element(elementLocator) != null) {
			Reporter.reportEvent(Reporter.MIC_PASS, successMessage);
			msg = successMessage;
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el elemento.");
			msg = "No se encuentra el elemento.";
		}
		return msg;
	}

// =========================================================================================================================================
	/**
	 * Valida que una cadena no sea null, no esté vacía ni contenga solo espacios en
	 * blanco.
	 *
	 * @param datoValidar la cadena a validar
	 * @return true si la cadena contiene al menos un carácter no vacío y no es
	 *         null; false en caso contrario
	 */
	protected boolean isValid(String datoValidar) {
		return datoValidar != null && !datoValidar.trim().isEmpty();
	}

// =========================================================================================================================================

	/**
	 * Verifica si un WebElement ubicado por un String es visible y habilitado en la
	 * página.
	 *
	 * @param locator el localizador del elemento (xpath)
	 * @return true si el elemento existe, está visible y habilitado; false en caso
	 *         contrario
	 */
	protected boolean isElementInteractable(String xpath) {
		WebElement element = this.element(xpath);
		return isElementInteractable(element);
	}

	/**
	 * Verifica si un WebElement ubicado por un By es visible y habilitado en la
	 * página.
	 *
	 * @param locator el localizador del elemento (By)
	 * @return true si el elemento existe, está visible y habilitado; false en caso
	 *         contrario
	 */
	protected boolean isElementInteractable(By locator) {
		WebElement element = this.element(locator);
		return isElementInteractable(element);
	}

	/**
	 * Verifica si un WebElement ya localizado es visible y habilitado.
	 *
	 * @param element el elemento WebElement
	 * @return true si el elemento es visible y habilitado; false en caso contrario
	 */
	protected boolean isElementInteractable(WebElement element) {
		return element != null && this.isDisplayed(element) && this.isEnabled(element);
	}

	/**
	 * Verifica si un elemento identificado por un localizador By existe en el DOM,
	 * pero NO está visible o habilitado.
	 *
	 * @param locator Localizador del elemento (By).
	 * @return true si el elemento existe pero no es visible o habilitado; false en
	 *         caso contrario.
	 */
	protected boolean isElementPresentAndNotInteractable(By locator) {
		WebElement el = this.element(locator);
		return el != null && (!this.isDisplayed(el) || !this.isEnabled(el));
	}

	/**
	 * Verifica si un elemento recibido directamente como WebElement existe, pero NO
	 * está visible o habilitado.
	 *
	 * @param element Elemento WebElement a validar.
	 * @return true si el elemento existe pero no es visible o habilitado; false en
	 *         caso contrario.
	 */
	protected boolean isElementPresentAndNotInteractable(WebElement element) {
		return element != null && (!this.isDisplayed(element) || !this.isEnabled(element));
	}

	/**
	 * Verifica si un elemento identificado por un localizador String (por ejemplo,
	 * un xpath) existe en el DOM, pero NO está visible o habilitado.
	 *
	 * @param locator Localizador del elemento (tipo String, por ejemplo un xpath).
	 * @return true si el elemento existe pero no es visible o habilitado; false en
	 *         caso contrario.
	 */
	protected boolean isElementPresentAndNotInteractable(String locator) {
		WebElement el = this.element(locator);
		return el != null && (!this.isDisplayed(el) || !this.isEnabled(el));
	}
}
