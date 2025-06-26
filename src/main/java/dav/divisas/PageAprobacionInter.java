package dav.divisas;

import org.openqa.selenium.By;

import dav.pymes.moduloCrearTx.ControllerCrearTx;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;

import dxc.util.DXCUtil;

public class PageAprobacionInter extends PageDivisas {

	private ControllerCrearTx controller = null;

// ==========================================================================================================================================================

	String btnAprobarTransaccionesSeleccionadas = "//*[@id='FormAprobar']/div[3]/div/table/tbody/tr/td[1]/div/button[1]";

	String filaLocator = "//*[@id='FormAprobar']/div[2]/div/table/tbody/tr[I]/td[3]/a[contains(text(), 'documentoTx')]";

	String checkboxFila = "//*[@id='FormAprobar']/div[2]/div/table/tbody/tr[I]/td[1]/*[@type='checkbox']";

	String timeLimiteAprobacion = "//*[@id='FormAprobar']/div[2]/div/table/tbody/tr[I]/td[11]/label[contains(text(),'00:00')]";

	String btnActualizarTasa = "//*[@id='FormAprobar']/div[2]/div/table/tbody/tr[I]/td[8]/label[@class='fondo-vencida']";

	String btnAceptarActualizarTasa = "//*[@id='botonModalAceptar']";
	String headerCampoAprobaciones = "//*[@id='FormAprobar']/div[2]/div/table/thead/tr[1]/th[J]";

	String camposAprobaciones = "//*[@id='FormAprobar']/div[2]/div/table/tbody/tr[I]/td[J]";
	By titulo = By.xpath("/html/body/div[2]/div[1]/div[1]/h2/string[contains(text(), 'Aprobaciones')]");;
	By subtitulo = By.xpath("/html/body/div[2]/div[1]/div[2]/h3[contains(text(), 'Transferencias pendientes de aprobación')]");
	By sesionEx = By.xpath("//b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]");
	By mensajenotificacion = By.xpath("//*[@id='mensaje']");
	String xpathNumDocumTxCon3 = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/preceding-sibling::td[4]";
	

// ==========================================================================================================================================================

	private static int contador = 1;

// ==========================================================================================================================================================

	public PageAprobacionInter(BasePageWeb parentPage) {
		super(parentPage);
	}

// ==========================================================================================================================================================

	/**
	 * inicioAprobaciones
	 * 
	 * @param primeroGuardar
	 * @return
	 * @throws Exception
	 */
	public String inicioAprobaciones(boolean primeroGuardar) throws Exception {
		
		String documentoTx = numAprova;
		
		String msg = null;

		msg = this.seleccionarTransferencia("Aprobaciones");// Se en carga de selecionar el modulo de Divisas
		
		if (isValid(msg))
			Reporter.reportEvent(Reporter.MIC_FAIL,msg);

		msg = waitForElementToAppear(btnAprobarTransaccionesSeleccionadas);
		if (isValid(msg)) {
			return msg;
		}

		// Validaciones adicionales de campos y titulos en la ventana de aprobaciones
		Reporter.reportEvent(Reporter.MIC_WARNING, "----------------TABLA INICIAL DE APROBACIONES------------------");
		Evidence.saveAllScreens("Registros Encontrados", this);

		msg = assertElementExists(titulo, "Se encuentra el título de 'Aprobaciones'");

		if (msg.equals("No se encuentra el elemento.")) {
			return msg;
		}
		
		msg = assertElementExists(subtitulo, "Se encuentra el subtítulo de 'Transferencias pendientes de aprobación'");
		
		if (msg.equals("No se encuentra el elemento.")) {
			return msg;
		}
		
		

//		String documentoTx = SettingsRun.getTestData().getParameter("Número Aprobación").trim();
//
//		// si realiza la consulta de pendiente con proceso guardar sin aprobar realiza
//		// la busqueda del docmentoTx con la hora exacta
//		if (primeroGuardar || !isValid(documentoTx)) {
//			String fecha = SettingsRun.getTestData().getParameter("Fecha tx").trim();
//			String hora = SettingsRun.getTestData().getParameter("Hora tx").trim();
//
//			// Intenta encontrar el documento con la hora original
//			documentoTx = findDocumentWithTimeAfterDelay(fecha, hora); // Esperar un minuto
//
//			if (!isValid(documentoTx)) {
//				// Si no lo encuentra, resta un minuto y vuelve a intentarlo
//				String modifiedHora1 = DXCUtil.subtractOneMinute(hora);
//				documentoTx = findDocumentWithTime(fecha, modifiedHora1);
//				if (!isValid(documentoTx)) {
//					// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//					String modifiedHora2 = DXCUtil.subtractOneMinute(modifiedHora1);
//					documentoTx = findDocumentWithTime(fecha, modifiedHora2);
//					if (!isValid(documentoTx)) {
//						// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//						String modifiedHora3 = DXCUtil.subtractOneMinute(modifiedHora2);
//						documentoTx = findDocumentWithTime(fecha, modifiedHora3);
//						if (!isValid(documentoTx)) {
//							// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//							String modifiedHora4 = DXCUtil.subtractOneMinute(modifiedHora3);
//							documentoTx = findDocumentWithTime(fecha, modifiedHora4);
//							if (!isValid(documentoTx)) {
//								// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//								String modifiedHora5 = DXCUtil.subtractOneMinute(modifiedHora4);
//								documentoTx = findDocumentWithTime(fecha, modifiedHora5);
//								if (!isValid(documentoTx)) {
//									// Si no se encuentra incluso después de los ajustes, muestra un mensaje de error
//									System.out.println("Error: Document not found with adjusted times.");
//								}
//							}
//						}
//					}
//				}
//			}
//
//		}
		
		if (!isValid(documentoTx)) 
			documentoTx = obtenerNumeroTxDocumentoGeneral("Aprobaciones");
//		documentoTx = ObtenerNumerodeTxDocumento2();
		
		/*
		 * Encuentra el registro por numero de documento Si encuentra el registro
		 * selecicona el checkbutton
		 */
		String xpathfilaLocator;
		String xpathcheckboxFila;
		String xpathtimeLimiteAprobacion;
		String xpathbtnActualizarTasa;
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			xpathfilaLocator = filaLocator.replace("I", String.valueOf(contador)).replace("documentoTx", documentoTx);
			
			if (this.element(xpathfilaLocator) != null) {
				// Almacena el numero de Aprobacion de la tx
				if (isValid(documentoTx))
					SettingsRun.getTestData().setParameter("Número Aprobación", documentoTx);
				
				xpathcheckboxFila = checkboxFila.replace("I", String.valueOf(contador));

				xpathtimeLimiteAprobacion = timeLimiteAprobacion.replace("I", String.valueOf(contador));

				xpathbtnActualizarTasa = btnActualizarTasa.replace("I", String.valueOf(contador));

				for (int j = 2; j < 12; j++) {
					
					String xpathheaderCampoAprobaciones = headerCampoAprobaciones.replace("J", String.valueOf(j));
					String xpathcamposAprobaciones = camposAprobaciones.replace("I", String.valueOf(contador)).replace("J", String.valueOf(j));

					String textHeader = this.element(xpathheaderCampoAprobaciones).getText();
					String textCampos = this.element(xpathcamposAprobaciones).getText();

					if (this.element(camposAprobaciones) != null) {
						Reporter.reportEvent(Reporter.MIC_PASS, "Se encuentra campo " + textHeader+ " no es nulo y no está vacío, con dato: " + textCampos);
					} else {
						Reporter.reportEvent(Reporter.MIC_FAIL,"El campo " + textHeader + ", es nulo o está vacío, con dato: " + textCampos);
					}
				}

				msg = waitForElementToAppear(xpathcheckboxFila);

				if (isValid(msg)) {
					return msg;
				}

				if (this.element(xpathtimeLimiteAprobacion) != null) {

					try {
						this.click(this.element(xpathbtnActualizarTasa));
						Reporter.reportEvent(Reporter.MIC_PASS,"Aparece botón de actualizar tasa y permite seleccionar");
					} catch (Exception e) {
						Reporter.reportEvent(Reporter.MIC_FAIL,"No aparece botón de actualizar tasa o no permite seleccionar");
					}

					msg = waitForElementToAppear(btnAceptarActualizarTasa);

					if (isValid(msg)) {
						return msg;
					}

					if (this.element(mensajenotificacion) != null) {
						String mensaje = findElement(mensajenotificacion).getText();
						Reporter.reportEvent(Reporter.MIC_PASS, "Aparece el mensaje: " + mensaje);
						
					} else {
						Reporter.reportEvent(Reporter.MIC_PASS,"No aparece el mensaje \"La tasa ha expirado. La cotización debe ser actualizada.\"");
					}

					String scriptClicIngresar1 = "document.querySelector(\"#botonModalAceptar\").click()";
					this.getJse().executeScript(scriptClicIngresar1);
					
					DXCUtil.wait(5);
				}

				msg = waitForElementToAppear(xpathcheckboxFila);
				
				if (isValid(msg)) {
					return msg;
				}

				try {
					
					this.focus(this.element(xpathcheckboxFila));
					this.click(this.element(xpathcheckboxFila));
					Reporter.reportEvent(Reporter.MIC_PASS,"Aparece el checkbox para seleccionar el registro y permite seleccionar");
					
				} catch (Exception e) {
					Reporter.reportEvent(Reporter.MIC_FAIL,"No aparece el checkbox para seleccionar el registro o no permite seleccionar");
				}
			}

			if (contador >= 30) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontraron registros");
				SettingsRun.exitTestIteration();
			}

		} while (this.element(xpathfilaLocator) == null);

		return null;
	}


//==========================================================================================================================================================


//	public String findDocumentWithTimeAfterDelay(String fecha, String horaconvert) throws Exception {
//		// Convierte minutos a milisegundos
//		return findDocumentWithTime(fecha, horaconvert);
//	}

//==========================================================================================================================================================

//	/**
//	 * Este metodo Obtiene el [Número de tx o Documento de la Tx]
//	 * @param fecha Tx
//	 * @param horaconvert Tx
//	 * @return Retorna el [Número de tx o Documento de la Tx]
//	 * @throws Exception
//	 */
//	public String findDocumentWithTime(String fecha, String horaconvert) throws Exception {
//		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
//		try {
//			String fechayHora = fecha + " " + horaconvert;
//			return this.element(xpathNumDocumTxCon3.replace("fechayhoraconvert", fechayHora).replace("MONEDA", moneda)).getText();
//		} catch (Exception e) {
//			return null;
//		}
//
//	}
//==========================================================================================================================================================
}
