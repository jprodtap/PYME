package dav.divisas;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;

import dxc.util.DXCUtil;

public class PageDocumentos_Y_Formularios extends PageDivisas {

	public PageDocumentos_Y_Formularios(BasePageWeb parentPage) {
		super(parentPage);
	}

// =======================================================================================================================

	// Busca el registro mediante la hora y el tipo de moneda
	String xpathNumDocumTxCon = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/following-sibling::td[contains(text(), 'DOCID')]/preceding-sibling::td/a";
	String xpathNumDocumTxObt = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/following-sibling::td[contains(text(), '')][3]";

	By TituloModulo = By.xpath("//button[contains(text(), 'Buscar')]");

	By campTitulo = By.xpath("//form[@id='declaracionForm']/div/h2");

	// Formulario
	By listConceptosCambiario = By.id("ConceptosCambiario");

	By cmpInfoNumdelformulario = By.id("NumeroFormulario");

	String tabInfoDatos = "//*[@id='table' and @style='display: none;' ]/table";
	String tabInfoDatos2 = "//*[@id='TABLExpath' and @style='display: none;' ]/table";
	String tabInfoDatosTx = "//*[@id='TABLENUM']/table";

	// -------------------------------[Tipo de
	// operación]--------------------------------------------------

	// -------------------------------[Endeudamiento externo y avales y garantías
	// Formulario 3]--------------------------------------------------
	By campInputNumerodelprestamooAval = By.id("descripcionOperacion_NumeroPrestamo");

	// Nombre del deudor o acreedor Avalado o beneficiario residente nombre del
	// deudor o la empresa ejemplo [EMP PYME 25]
	By campInputdescripcionOperacion_NombreDeudorAcredor = By.id("descripcionOperacion_NombreDeudorAcredor");
	By campSeletListaTipoIdentificacion = By.id("descripcionOperacion_TipoIdentificacion");
	By campInputNumeroIdentificacion = By.id("descripcionOperacion_NumeroIdentificacion");// ejemplo el numero de
																							// nit[EMP PYME 25]
	By campInputDigitoVerificacion = By.id("descripcionOperacion_DigitoVerificacion");
	By campSeletListaMonedaEstipulada = By.id("descripcionOperacion_MonedaEstipulada");
	By campInputValorMoneda = By.id("descripcionOperacion_ValorMoneda");
	By campInputTasadeCambioMonedaEstipulada = By.id("descripcionOperacion_TRM");

	// Nombre del acreedor (Créditos pasivos) o el deudor (Créditos activos) o
	// avalista
	By campInputNombreAcredorDeudorAvalista = By.id("descripcionOperacion_NombreAcredorDeudorAvalista");

//-------------------------------[Inversiones Internacionales]--------------------------------------------------

	By listDestinodelainversion = By.id("tipoInversionId");

	By listSeleOpcionInversionId = By.id("opcionInversionId");

	// ----------------[EMPRESA Receptora o Administrador
	// (Portafolio)]--------------------------------
	By listSeleOpcionempresaReceptoraTipoIdentificacion = By.id("empresaReceptora_TipoIdentificacion");
	By imputempresaReceptoraNumeroIdentificacion = By.id("empresaReceptora_NumeroIdentificacion");
	By imputempresaReceptoraDigitoVerificacion = By.id("empresaReceptora_DigitoVerificacion");
	By imputempresaReceptoraNombreRazonSocial = By.id("empresaReceptora_NombreRazonSocial");
	By empresaReceptoraCodigoDepartamento_NHidden = By.id("empresaReceptora_CodigoDepartamento_NHidden");
	By empresaReceptoraCodigoPais_NHidden = By.id("empresaReceptora_CodigoPais_NHidden");
	By empresaReceptoraCodigoCiudad = By.id("empresaReceptora_CodigoCiudad");
	By empresaReceptoraCodigoCIUU = By.id("empresaReceptora_CodigoCIUU");
	By empresaReceptoraTelefono = By.id("empresaReceptora_Telefono");
	By empresaReceptoraDireccion = By.id("empresaReceptora_Direccion");
	By empresaReceptoraCorreo = By.id("empresaReceptora_Correo");
	By empresaReceptoraSector = By.id("empresaReceptora_Sector");
	By empresaReceptoraTipoEmpresa = By.id("empresaReceptora_TipoEmpresa");
	By empresaReceptoraSuperintendencia = By.id("empresaReceptora_Superintendencia");
	By selectempresaReceptora_Naturaleza = By.id("empresaReceptora_Naturaleza");

	// ----------------[Identificación del Inversionista (Nacional o
	// extranjero)]--------------------------------

	By listSeleOpcioninversionistaTipoIdentificacion = By.id("inversionista_TipoIdentificacion");

	By imputinversionistaNumeroIdentificacion = By.id("inversionista_NumeroIdentificacion");

	By imputinversionista_DigitoVerificacion = By.id("inversionista_DigitoVerificacion");

	By imputinversionista_NombreRazonSocial = By.id("inversionista_NombreRazonSocial");
	By imputinversionista_CodigoDepartamento_NHidden = By.xpath("//*[@id='inversionista_CodigoDepartamento_NHidden']");
	By selectinversionista_CodigoCiudad = By.id("inversionista_CodigoCiudad");
	By selectinversionista_CodigoPais = By.id("inversionista_CodigoPais");
	By imputinversionista_CodigoCIUU_NHidden = By.id("inversionista_CodigoCIUU_NHidden");
	By selectinversionista_Naturaleza = By.id("inversionista_Naturaleza");
	By imputinversionista_Telefono = By.id("inversionista_Telefono");
	By imputinversionista_Correo = By.id("inversionista_Correo");
	By imputinversionista_Direccion = By.xpath("//*[@id='inversionista_Direccion']");
	By selectinversionista_Sector = By.id("inversionista_Sector");
	By selectinversionista_TipoEmpresa = By.id("inversionista_TipoEmpresa");
	By selectinversionista_Superintendencia = By.id("inversionista_Superintendencia");

//---------------------------------------------------------------------------------

	By listNumeralOperacion_Numeral1 = By.id("numeralOperacion_Numeral");
	By ListNumeralOperacion_Numeral0id = By.id("numerales_0__Numeral");

	By ListNumeralOperacion_Numeral1id = By.id("numerales_1__Numeral");
	String ListNumeralOperacion_Numeral2id = "numerales_NUM__Numeral";

	By inputValorMonedaGiro = By.id("numerales_0__ValorMonedaGiro");
	By inputValorMonedaGiro1 = By.id("numerales_1__ValorMonedaGiro");
	String inputValorMonedaGiro2 = "numerales_NUM__ValorMonedaGiro";

	By inputValorMonedaNegociacion = By.id("numerales_0__ValorMonedaNegociacion");
	By inputValorMonedaNegociacion1 = By.id("numerales_1__ValorMonedaNegociacion");
	String inputValorMonedaNegociacion2 = "numerales_NUM__ValorMonedaNegociacion";

	By inputValorMonedaEstipulada = By.id("numerales_0__ValorMonedaEstipulada");
	By inputValorMonedaEstipulada1 = By.id("numerales_1__ValorMonedaEstipulada");
	String inputValorMonedaEstipulada2 = "numerales_NUM__ValorMonedaEstipulada";

	By inputValorenUSD = By.id("numerales_0__ValorUSD");
	By inputValorenUSD1 = By.id("numerales_1__ValorUSD");
	String inputValorenUSD2 = "numerales_NUM__ValorUSD";

	By inputOperacion_NumeroDeposito = By
			.xpath("//*[@id='numerales_0__NumeroDeposito' or @id='numeralOperacion_NumeroDeposito']");
	By inputOperacion_NumeroDeposito1 = By.xpath("//*[@id='numerales_1__NumeroDeposito']");
	String inputOperacion_NumeroDeposito2 = "numerales_NUM__NumeroDeposito";

	// ----------------------[Inversion Internacionales]-----------------------

	By inputNumeroDeclaracion = By.id("numerales_0__NumeroDeclaracion");
	By inputNumeroDeclaracion1 = By.id("numerales_1__NumeroDeclaracion");
	String inputNumeroDeclaracion2 = "numerales_NUM__NumeroDeclaracion";

	// ----------------------[Inversion Internacionales]-----------------------

	By inputnumeralOperacion_Participaciones = By.xpath("//input[@id='numeralOperacion_Participaciones']");
	By seleListnumeralOperacion_MotivosSinParticipacion = By
			.xpath("//*[@id='numeralOperacion_MotivosSinParticipacion']");

	// ----------------------[Exportacion de bienes]-----------------------

	By labelDeduciones = By.xpath("//*[@id='numeralExtendidoContainer']/div/div[3]/div[1]/div/label");
	By inputnumeralExtendidoDeducciones = By.xpath("//*[@id='numeralExtendido_Deducciones']");

	By btnSeleccioneunarchivo = By.id("uploadBtn");

	By btnEnviar = By.id("enviarForm");

	String tipoOperacionXpath = "//label[text()='OPERACION']//parent::div//span";

// ----------------[Información documentos de importación - DIAN]-------------------------------------------

	String inputlisDeclaraciondeimportacionNo = "documentosImportacion_NUM__DeclaracionImportacionNro";
	String inputValorUSD = "documentosImportacion_NUM__ValorUSD";

	String predicDATO = "//li[contains(text(), 'DATOBUSCAR')]";

// ----------------[Cargue de archivo]---------------------------------------------------------------------

	String archivoCargado = "//*[@id='document-form-card-container']/documents-form-card//*[contains(text(), 'NOMARCH')]";

	// PSEUDOELEMENTO
	String strPseudoElemento = "return window.getComputedStyle(arguments[0],'::after').getPropertyValue('background-color')";
// =======================================================================================================================

	By CerrSesion = By.xpath("//*[@id='CerrarSesion']");
	By sesionEx = By.xpath("//b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]");

// =======================================================================================================================

	private static final String Importaciones = "Importaciones de bienes", Exportaciones = "Exportaciones de bienes",
			Endeudamiento = "Endeudamiento externo y avales y garantías", Inversiones = "Inversiones internacionales",
			ServTryOtros = "Servicios, transferencias y otros conceptos";

	private static final String Inicial = "Inicial", Devolucion = "Devolución";

//	static String numAprova = null;

	int contador = 0;

// ========================================[getNumAprInterna]===============================================================================

//	/**
//	 * Llama el dato de numero de aprobacion desde la transaccion
//	 */
//	public void SetNumAprInterna(String numAproTx) {
//		numAprova = numAproTx;
//	}

// ============================================[ModuloDocumetosYFormularios]===========================================================================

	/**
	 * Metodo Verifica si esta en el modulo Documentos y Formularios si no ingresa
	 * al modulo
	 * 
	 * @return
	 * @throws Exception
	 */

	public String ModuloDocumetosYFormularios(String tipoPrueba) throws Exception {
		String msg = null;

		msg = validarAlertDocuc();

		if (isValid(msg) && !msg.contains("En este módulo puede visualizar las operaciones")&& !msg.contains("Los campos que no se presentan en la declaración de cambio serán autocompletados"))
			return msg;

//		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();

		if (tipoPrueba.equals("Tx Pend Aprobación")) {
			IralModuloDocumetosYFormularios();
		}

		contador = 0;
		WebElement Titulo = null;
		do {
			Titulo = this.element(campTitulo);
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				Evidence.save("No se encuentra el Titulo Documentos y formularios");
				this.click(CerrSesion);
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el Titulo: Documentos y formularios");
				return "No se encuentra el Titulo: Documentos y formularios";
			}
			contador++;

		} while (this.element(Titulo) == null || !this.element(Titulo).isDisplayed());

		return msg;

	}

// =============================================[IralModuloDocumetosYFormularios]==========================================================================

	/**
	 * Metodo ingresa al modulo de Documentos y Formularios y realiza la consulta de
	 * la Tx realizada en el portal
	 * 
	 * @return
	 * @throws Exception
	 */
	public String IralModuloDocumetosYFormularios() throws Exception {

		WebElement moduloDoc = null;
		String documentoTx = null;
		String msg = null;

		this.switchToFrameDivisas();

		msg = this.seleccionarTransferencia("Documentos y Formularios");// Se en carga de selecionar el modulo de
																		// Divisas

		if (isValid(msg))
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el modulo: Documentos y Formularios");

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(3);
			if (this.switchToFrameDivisas())
				moduloDoc = this.element(TituloModulo);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				Evidence.save("No se encuentra el modulo: Documentos y Formularios");
				this.click(CerrSesion);
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el modulo: Documentos y Formularios");
				SettingsRun.exitTestIteration();
			}

		} while (this.element(moduloDoc) == null || !moduloDoc.isDisplayed());

//		if (!isValid(numAprova)) {
//			
//			numAprova = SettingsRun.getTestData().getParameter("Número Aprobación");
//			
//			DXCUtil.wait(5);
//			
//			if (isValid(numAprova)) {
//				
//				documentoTx = xpathNumDocumTxCon.replace("fechayhoraconvert", "").replace("MONEDA", "").replace("DOCID", numAprova);
//				
//				if (this.element(documentoTx) == null)
//					Reporter.reportEvent(Reporter.MIC_FAIL, "El numero de Aprobacion no se encontro: " + numAprova);
//				
//			} else {
//				
//				numAprova = null;
//			}
//
//		}

		msg = ObtenerNumerodeTxDocumento();

//		if (!isValid(numAprova)) {
//
////			String fecha = SettingsRun.getTestData().getParameter("Fecha tx").trim();
//			String hora = SettingsRun.getTestData().getParameter("Hora tx").trim();
//			Reporter.reportEvent(Reporter.MIC_INFO, "Hora de la Tx : " + hora);
//
//			// Intenta encontrar el registro con la hora original
//			documentoTx = findDocumentWithTime(hora);
//			
//			if (documentoTx == null) {
//			// Intenta encontrar el registro con la hora original modificada 1 minuto antes
//			String hora2 = DXCUtil.subtractOneMinute(hora);
//			Reporter.write(hora2);
//			documentoTx = findDocumentWithTime(hora2);
//			}
//			
//			if (documentoTx == null) {
//				// Intenta encontrar el registro con la hora MODIFICADA
//				String horaM = DXCUtil.horaAdd("HH:mm", hora, 3);
//				documentoTx = findDocumentWithTime(horaM); // Esperar un minuto
//				Evidence.save("Modulo - Documentos Y Formularios");
//				if (documentoTx == null) {
//					// Si no lo encuentra, resta un minuto y vuelve a intentarlo
//					String modifiedHora1 = DXCUtil.subtractOneMinute(horaM);
//					documentoTx = findDocumentWithTime(modifiedHora1);
//					if (documentoTx == null) {
//						// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//						String modifiedHora2 = DXCUtil.subtractOneMinute(modifiedHora1);
//						documentoTx = findDocumentWithTime(modifiedHora2);
//						if (documentoTx == null) {
//							// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//							String modifiedHora3 = DXCUtil.subtractOneMinute(modifiedHora2);
//							documentoTx = findDocumentWithTime(modifiedHora3);
//							if (documentoTx == null) {
//								// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//								String modifiedHora4 = DXCUtil.subtractOneMinute(modifiedHora3);
//								documentoTx = findDocumentWithTime(modifiedHora4);
//								if (documentoTx == null) {
//									// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//									String modifiedHora5 = DXCUtil.subtractOneMinute(modifiedHora4);
//									documentoTx = findDocumentWithTime(modifiedHora5);
//									if (documentoTx == null) {
//										String modifiedHora6 = DXCUtil.subtractOneMinute(modifiedHora5);
//										documentoTx = findDocumentWithTime(modifiedHora6);
//										if (documentoTx == null) {
//											// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//											String modifiedHora7 = DXCUtil.subtractOneMinute(modifiedHora6);
//											documentoTx = findDocumentWithTime(modifiedHora7);
//											if (documentoTx == null) {
//												// Si no se encuentra incluso después de los ajustes, muestra un mensaje de error
//												Reporter.reportEvent(Reporter.MIC_FAIL,"Error: Documento no encontrado con tiempos ajustados.");
//												this.getDriver().switchTo().defaultContent();
//												Evidence.save("Modulo - Documentos Y Formularios");
//												this.click(CerrSesion);
//												SettingsRun.exitTestIteration();
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//
//		}

//		contador = 0;
//		
//		WebElement documenTxabuscar = null;
//
//		do {
//			contador++;
//			DXCUtil.wait(3);
//			documenTxabuscar = this.element(documentoTx);
//			if (contador >= 30) {
//				this.getDriver().switchTo().defaultContent();
//				return "No se encuentra TX en Documentos Y Formularios";
//			}
//		} while (documenTxabuscar == null || !documenTxabuscar.isDisplayed());
//
//		// Almacena el numero de Aprobacion de la tx
//		if (isValid(numAprova))
//			SettingsRun.getTestData().setParameter("Número Aprobación", numAprova);
//
//		this.click(documenTxabuscar);
		if (isValid(msg) && msg.equals("Error: Documento no encontrado con tiempos ajustados."))
			return msg;

		Evidence.save("Modulo - Documentos Y Formularios TX");

		DXCUtil.wait(3);

		msg = validarAlertDocuc();

		if (isValid(msg))
			return msg;

		return null;
	}

// ============================================[DatosDocumetosYFormularios]===========================================================================

	
	/**
	 * Metodo Realiza el flujo completo y llenado de los datos de Documetos Y
	 * Formularios
	 * 
	 * @param conceptosCambiario
	 * @param tipoOperacion
	 * @param tipoDestinodelainversion
	 * @param opcionInversionId
	 * @param valorTx
	 * @param numeral1
	 * @param valorNumeral1
	 * @param numCambiario2
	 * @param valorNumeral2
	 * @param numeroDeposito
	 * @param numeroFacturaodeclaración
	 * @param valordeduciones
	 * @param cargarArchivo
	 * @return
	 * @throws Exception
	 */
	public String DatosDocumetosYFormularios(String conceptosCambiario, String tipoOperacion,
			String tipoDestinodelainversion, String opcionInversionId, String valorTx, String numeral1,
			String valorNumeral1, String numCambiario2, String valorNumeral2, String valordeduciones,
			String... cargarArchivo) throws Exception {

		WebElement listConceptodelatransferencia = null;

		String msg = null;

		contador = 0;

		msg = validarAlertDocuc();

		if (isValid(msg))
			return msg;

		// --------------------Concepto de la transferencia: ------------------- //
		do {
			DXCUtil.wait(2);
			listConceptodelatransferencia = this.element(listConceptosCambiario);
			contador++;
			if (contador >= 30) {
				msg = "No se presento el campo Concepto de la transferencia";
				if (this.element(sesionEx) != null) {
					msg = this.element(sesionEx).getText();
					if (isValid(msg)) {
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					}
					Evidence.save("No se presento el campo Concepto de la transferencia");
					if (this.element(btnEnviar) != null) {
						this.click(btnEnviar);
					}
					if (isValid(msg))
						return msg;
				}

				Evidence.save("No se presento el campo Concepto de la transferencia");
				this.click(btnEnviar);
				return "No se presento el campo Concepto de la transferencia";
			}

		} while (this.element(listConceptodelatransferencia) == null && !this.isDisplayed(listConceptodelatransferencia)&& !this.isSelected(listConceptodelatransferencia) && !this.isEnabled(listConceptodelatransferencia));

		Evidence.save("Campos-Documentos");

		if (isValid(conceptosCambiario)) {
			DXCUtil.wait(5);
			msg = this.selectListItem(listConceptosCambiario, conceptosCambiario);
			Evidence.save("Campo-Seleccione Concepto de la transferencia");
			if (isValid(msg) && !msg.contains("Valor no encontrado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}

			String cambiarConcepto = SettingsRun.getTestData().getParameter("Cambiar Concepto de la transferencia");

			if (cambiarConcepto.equals("SI")) {
				String conceptoAcambiar = SettingsRun.getTestData().getParameter("Concepto de la transferencia A Cambiar");
				conceptosCambiario = conceptoAcambiar;
				
				msg = this.selectListItem(listConceptosCambiario, conceptoAcambiar);
				Evidence.save("Campo-Seleccione Concepto de la transferencia");
				
				if (isValid(msg) && !msg.contains("Valor no encontrado")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					this.click(btnEnviar);
					msg = this.closeActiveIntAlertConfirma();
					if (isValid(msg)) {
						return msg;
					}
				}
				
			}

		} else {
			// el campo esta vacio da clic al boton siguiente
			Evidence.save("Campo-Seleccione concepto de la transferencia");
			this.click(btnEnviar);
			msg = this.closeActiveIntAlertConfirma();
			if (isValid(msg))
				return msg;
		}

		// --------------------Número del formulario: -------------------

		String numFormulario = this.element(cmpInfoNumdelformulario).getText();
		Reporter.reportEvent(Reporter.MIC_INFO, numFormulario);

		// -------------------- Tabla información transferencia -------------------

		this.ValidacionTablasInformativa();

		// --------------------[Tipo de operación] -------------------
		if (isValid(conceptosCambiario)) {

			ValidacionTipodeoperacion(conceptosCambiario, tipoOperacion);
		}

		// --------------------[Detalles de destino de la inversión] -------------------

		if (conceptosCambiario.contains(Inversiones) && isValid(tipoDestinodelainversion)) {

			// --------------------Destino de la inversión: -------------------

			WebElement listTipodelainversion = null;
			contador = 0;
			do {
				DXCUtil.wait(5);
				listTipodelainversion = this.element(listDestinodelainversion);
				contador++;
				if (contador > 30) {
					msg = "No se presento el campo Destino de la inversión";
					if (this.element(sesionEx) != null) {
						msg = this.element(sesionEx).getText();
						if (isValid(msg)) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

						Evidence.save("No se presento el campo Destino de la inversión");

						if (this.element(btnEnviar) != null) {
							this.click(btnEnviar);
						}

						if (isValid(msg))
							return msg;
					}

					Evidence.save("No se presento el campo Destino de la inversión");
					this.click(btnEnviar);
					return "No se presento el campo Destino de la inversión";
				}

			} while (this.element(listTipodelainversion) == null && this.isEnabled(listTipodelainversion) == false
					&& this.isDisplayed(listTipodelainversion) == false);

			if (isValid(conceptosCambiario)) {
				msg = this.seleOption(listDestinodelainversion, tipoDestinodelainversion);
				Evidence.save("Campo-Seleccione Destino de la inversión");

				if (msg.isEmpty()) {
					Reporter.reportEvent(Reporter.MIC_FAIL,
							"Elemento [" + tipoDestinodelainversion + "] NO presentado");
					this.click(btnEnviar);
//					DXCUtil.wait(5);
					msg = this.closeActiveIntAlertConfirma();
					if (isValid(msg)) {
						return msg;
					}
				}

			} else {
				// el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione Destino de la inversión");
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg))
					return msg;
			}

			// --------------------Opción de inversión: -------------------

			WebElement listOpcionInversionId = null;

			contador = 0;

			do {

				DXCUtil.wait(3);

				listOpcionInversionId = this.element(listSeleOpcionInversionId);

				contador++;

				if (contador >= 30) {

					msg = "No se presento el campo Opción de inversión";

					if (this.element(sesionEx) != null) {
						msg = this.element(sesionEx).getText();

						if (isValid(msg)) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

						Evidence.save("No se presento el campo Opción de inversión");

						if (this.element(btnEnviar) != null) {
							this.click(btnEnviar);
						}

						if (isValid(msg))
							return msg;
					}

					Evidence.save("No se presento el campo Opción de inversión");
					this.click(btnEnviar);
					return "No se presento el campo Opción de inversión";
				}

			} while (this.element(listOpcionInversionId) == null || this.isEnabled(listOpcionInversionId) == false
					&& this.isDisplayed(listOpcionInversionId) == false);

			if (isValid(conceptosCambiario)) {

				msg = this.seleOptionGetAttribu(listSeleOpcionInversionId, opcionInversionId);
				Evidence.save("Campo-Seleccione Opción de inversión");

				if (!isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					this.click(btnEnviar);
					msg = this.closeActiveIntAlertConfirma();
					if (isValid(msg)) {
						return msg;
					}
				}

			} else {
				// el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione Opción de inversión");
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg))
					return msg;
			}

		}

		// --------------------Descripción de la operación: -------------------

		String elementoSelecionado = ValidacionListaNumeral1(conceptosCambiario);

		if (elementoSelecionado.equals("No se presento el campo Numeral")
				|| elementoSelecionado.contains("Error mensaje:")) {
			return elementoSelecionado;
		}

		String numeroDeposito = SettingsRun.getTestData().getParameter("Número de depósito 1");
		String numeroFacturaoReferDeclaracion = SettingsRun.getTestData().getParameter("Número de declaración 1");

		if (isValid(conceptosCambiario)) {

			Evidence.save("Descripción de la operación");

			String cambiarlistnumeralOperacion_Numeral1 = SettingsRun.getTestData().getParameter("Cambiar Numeral cambiario 1");
			String numeral1Acambiar = SettingsRun.getTestData().getParameter("Numeral cambiario A Cambiar 1");
			String cambiarConcepto = SettingsRun.getTestData().getParameter("Cambiar Concepto de la transferencia");

			if (cambiarConcepto.equals("SI")) {
				String conceptoAcambiar = SettingsRun.getTestData()
						.getParameter("Concepto de la transferencia A Cambiar");
				conceptosCambiario = conceptoAcambiar;
			}

			if (conceptosCambiario.contains(Inversiones)) {

//--------------------------------[Identificación del inversionista (Nacional o extranjero)]-------------------------------------------------------------------

				DXCUtil.wait(5);
				boolean Portafolio = false;
				boolean Inversionista = false;

				switch (opcionInversionId) {
				case "Empresas":
				case "Empresas (EMPA)":
				case "Capital asignado sucursal régimen especial  (CSRE)":
				case "Capital asignado sucursal régimen general (CSRG)":
				case "Capital asignado sucursales Bancos y Compañias de seguros (CSBC)":
				case "Inversión suplementaria al capital asignado sucursales de sociedades extranjeras (ISDC)":
				case "Bonos obligatoriamente convertibles en acciones (BOCE)":
				case "Patrimonios Autónomos (PATO)":
				case "Encargos fiduciarios (EFDC)":
				case "Actos o contratos sin participación en el capital (ACON)":
				case "Fondos de capital privado (FCPR)":
					Portafolio = true;
					Inversionista = true;
					break;
				case "Establecimientos de comercio (EDCO)":
				case "Inmuebles (INMU)":
				case "Activos Intangibles (AINT)":
				case "Inversión de portafolio":
				case "Inversión financiera y en activos en el exterior":
					Inversionista = true;
					break;

				default:
					break;
				}

				// ----------------[EMPRESA Receptora o Administrador(Portafolio)]--------------------------------

				if (Portafolio) {
					String tipodeidentificacionReceptora = SettingsRun.getTestData().getParameter("Empresa receptora - Tipo de identificación");
					String numerodeidentificacionReceptora = SettingsRun.getTestData().getParameter("Empresa receptora - Número de identificación");
					String digitodeverificacionReceptora = SettingsRun.getTestData().getParameter("Empresa receptora - Dígito de verificación");
					String nombreorazonsocialReceptora = SettingsRun.getTestData().getParameter("Empresa receptora - Nombre o razón social");
					String codigopaisReceptora = SettingsRun.getTestData().getParameter("Empresa receptora - Código país");
					String codigodepartamentoReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Código departamento");
					String codigociudadReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Código ciudad");
					String codigoCIIUReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Código CIIU");
					String telefonoReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Teléfono");
					String direccionReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Dirección");
					String correoReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Correo electrónico");
					String sectorReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Sector");
					String tipodeempresaReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Tipo de empresa");
					String superintendenciaReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Superintendencia de vigilancia");
					String actividadReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Actividad");
					String tipoderegimenReptora = SettingsRun.getTestData().getParameter("Empresa receptora - Tipo de régimen");
					String naturalezaReptora = SettingsRun.getTestData().getParameter("Empresa receptora  - Naturaleza");

					if (!isValid(tipodeidentificacionReceptora)) {
						tipodeidentificacionReceptora = "NIT";
					}

					if (!isValid(numerodeidentificacionReceptora)) {
						numerodeidentificacionReceptora = "1021374642";
					}

					if (!isValid(digitodeverificacionReceptora)) {
						digitodeverificacionReceptora = "1";
					}

					if (!isValid(nombreorazonsocialReceptora)) {
						nombreorazonsocialReceptora = "Davivienda";
					}

					if (!isValid(codigopaisReceptora)) {
						codigopaisReceptora = "CO";
					}

					if (!isValid(codigodepartamentoReptora)) {
						codigodepartamentoReptora = "BOGOTA";
					}
					if (!isValid(codigociudadReptora)) {
						codigociudadReptora = "BOGOTA";
					}

					if (!isValid(codigoCIIUReptora)) {
						codigoCIIUReptora = "115";
					}

					if (!isValid(telefonoReptora)) {
						telefonoReptora = "5121457";
					}

					if (!isValid(direccionReptora)) {
						direccionReptora = "CALLE 190 # 75";
					}

					if (!isValid(correoReptora)) {
						correoReptora = "davivienda@gmail.com";
					}

					if (!isValid(sectorReptora)) {
						sectorReptora = "Público";
					}

					if (!isValid(tipodeempresaReptora)) {
						tipodeempresaReptora = "1. Sociedades / Empresas unipersonales";
					}

					if (!isValid(superintendenciaReptora)) {
						superintendenciaReptora = "2. Superintendencia Financiera de Colombia";
					}

					if (!isValid(actividadReptora)) {
						actividadReptora = "Exploración y explotación de hidrocarburos o minería";
					}

					if (!isValid(tipoderegimenReptora)) {
						tipoderegimenReptora = "Especial";
					}

					if (!isValid(naturalezaReptora)) {
						naturalezaReptora = "Persona natural";
					}

					this.EmpresaReceptoraoAdministrador(this.getTipoCuenta(tipodeidentificacionReceptora),
							numerodeidentificacionReceptora, digitodeverificacionReceptora, nombreorazonsocialReceptora,
							codigopaisReceptora, codigodepartamentoReptora, codigociudadReptora, codigoCIIUReptora,
							telefonoReptora, direccionReptora, correoReptora, this.getSector(sectorReptora),
							this.getTipoEmpresa(tipodeempresaReptora), naturalezaReptora,
							this.getSuperintendencia(superintendenciaReptora));
				}

//-------------------------------------------------------------------------------------------------------------------------

				// ----------------[Identificación del Inversionista (Nacional o
				// extranjero)]--------------------------------

				if (Inversionista) {

					String identificacionInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Tipo de identificación");
					String numerodeidentificacionInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Número de identificación");
					String digitodeverificacionInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Dígito de verificación");
					String nombreorazonsocialInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Nombre o razón social");

					String codigoPaisInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Código país");
					String codigoDepartamentoInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Código departamento");
					String codigociudadInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Código ciudad");
					String codigoCIIUInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Código CIIU");

					String correoElectronicoInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Correo electrónico");
					String sectorInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Sector");
					String tipodeempresaInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Tipo de empresa");
					String superintendenciaInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Superintendencia de vigilancia");
					String naturalezaInversionista = SettingsRun.getTestData()
							.getParameter("Identificación del inversionista - Naturaleza");

					String telefonoInversionista = "";
					String direccionInversionista = "";

					if (SettingsRun.getTestData().parameterExist("Identificación del inversionista - Teléfono"))

						telefonoInversionista = SettingsRun.getTestData()
								.getParameter("Identificación del inversionista - Teléfono");

					if (SettingsRun.getTestData().parameterExist("Identificación del inversionista - Teléfono"))
						direccionInversionista = SettingsRun.getTestData()
								.getParameter("Identificación del inversionista - Dirección");

					if (!isValid(identificacionInversionista)) {
						identificacionInversionista = "NIT";
					}
					if (!isValid(numerodeidentificacionInversionista)) {
						numerodeidentificacionInversionista = "1084525645";
					}

					if (!isValid(digitodeverificacionInversionista)) {
						digitodeverificacionInversionista = "1";
					}

					if (!isValid(nombreorazonsocialInversionista)) {
						nombreorazonsocialInversionista = "AUTOMATIZACION";
					}

					if (!isValid(codigoPaisInversionista)) {
						codigoPaisInversionista = "CO";
					}

					if (!isValid(codigoDepartamentoInversionista)) {
						codigoDepartamentoInversionista = "BOGOTA";
					}

					if (!isValid(codigociudadInversionista)) {
						codigociudadInversionista = "BOGOTA";
					}

					if (!isValid(codigoCIIUInversionista)) {
						codigoCIIUInversionista = "115";
					}

					if (!isValid(correoElectronicoInversionista)) {
						correoElectronicoInversionista = "davivienda@gmail.com";
					}

					if (!isValid(sectorInversionista)) {
						sectorInversionista = "Público";
					}

					if (!isValid(tipodeempresaInversionista)) {
						tipodeempresaInversionista = "1. Sociedades / Empresas unipersonales";
					}

					if (!isValid(superintendenciaInversionista)) {
						superintendenciaInversionista = "3. Superintendencia de Puertos y Transporte";
					}

					if (!isValid(naturalezaInversionista)) {
						naturalezaInversionista = "Persona natural";
					}

					if (!isValid(telefonoInversionista)) {
						telefonoInversionista = "5121457"; // o el valor que necesites
					}

					if (!isValid(direccionInversionista)) {
						direccionInversionista = "CALLE 190 # 75"; // o el valor que necesites
					}

					this.IdentificacióndelInversionista(this.getTipoCuenta(identificacionInversionista),
							numerodeidentificacionInversionista, digitodeverificacionInversionista,
							nombreorazonsocialInversionista, codigoPaisInversionista, codigoDepartamentoInversionista,
							codigociudadInversionista, codigoCIIUInversionista, telefonoInversionista,
							direccionInversionista, correoElectronicoInversionista, this.getSector(sectorInversionista),
							this.getTipoEmpresa(tipodeempresaInversionista), naturalezaInversionista,
							this.getSuperintendencia(superintendenciaInversionista));

				}

//-------------------------------------------------------------------------------------------------------------------------

				// ----------------[Descripción de la
				// operaciónInversiones]--------------------------------

				msg = null;

				if (!isValid(elementoSelecionado) || elementoSelecionado.equals("No hay ninguna opción seleccionada.")
						|| elementoSelecionado.equals("Seleccione un numeral")) {

					if (this.element(listNumeralOperacion_Numeral1) != null) {
						msg = this.selectListItemExacto(listNumeralOperacion_Numeral1, numeral1);

						if (cambiarlistnumeralOperacion_Numeral1.equals("SI")) {

							numeral1Acambiar = SettingsRun.getTestData().getParameter("Numeral cambiario A Cambiar 1");

							if (this.element(listNumeralOperacion_Numeral1) != null) {

								msg = this.selectListItemExacto(listNumeralOperacion_Numeral1, numeral1Acambiar);

								if (isValid(msg)) {

									String[] Listanumeral = this.getListItems(listNumeralOperacion_Numeral1);
									Reporter.write("Lista Disponible");

									for (String numerales : Listanumeral) {
										Reporter.write(numerales);
									}

									return msg;
								}
							}

						} else {

							if (isValid(msg)) {

								String[] Listanumeral = this.getListItems(listNumeralOperacion_Numeral1);
								Reporter.write("Lista Disponible");

								for (String numerales : Listanumeral) {
									Reporter.write(numerales);
								}

								return msg;
							}
						}

					}

				} else {
					Reporter.reportEvent(Reporter.MIC_INFO, "Valor ya seleccionado: " + elementoSelecionado + "");
				}

				if (cambiarlistnumeralOperacion_Numeral1.equals("SI")) {

					numeral1Acambiar = SettingsRun.getTestData().getParameter("Numeral cambiario A Cambiar 1");

					if (this.element(listNumeralOperacion_Numeral1) != null) {
						msg = this.selectListItemExacto(listNumeralOperacion_Numeral1, numeral1Acambiar);
						if (isValid(msg)) {
							return msg;
						}
					}
				}

				// ------------------------------------------------------------------------------------------

			} else {

				msg = null;

				// ----------------[Descripción de la
				// operaciónInversiones]--------------------------------
				cambiarConcepto = SettingsRun.getTestData().getParameter("Cambiar Concepto de la transferencia");

				if (cambiarConcepto.equals("NO")) {

					if (!isValid(elementoSelecionado)
							|| elementoSelecionado.equals("No hay ninguna opción seleccionada.")
							|| elementoSelecionado.equals("Seleccione un numeral")) {

						if (this.element(listNumeralOperacion_Numeral1) != null) {
							msg = this.selectListItemExacto(listNumeralOperacion_Numeral1, numeral1);
						}

						if (this.element(ListNumeralOperacion_Numeral0id) != null) {
							msg = this.selectListItemExacto(ListNumeralOperacion_Numeral0id, numeral1);
						}

						if (isValid(msg)) {
							return msg;
						}

					} else {
						Reporter.reportEvent(Reporter.MIC_INFO, "Valor ya seleccionado: " + elementoSelecionado + "");
					}
				}

				if (conceptosCambiario.contains(Endeudamiento)) {

					String cambiarDatosDescripciondelaoperacion = SettingsRun.getTestData()
							.getParameter("Cambiar Datos Descripción de la operación");

					String numerodelprestamooaval = SettingsRun.getTestData()
							.getParameter("Número del préstamo o aval");

					this.write(campInputNumerodelprestamooAval, numerodelprestamooaval);

					String nombredelacreedoroeldeudoroavalista = SettingsRun.getTestData()
							.getParameter("Nombre del acreedor o el deudor o avalista");

					this.write(campInputNombreAcredorDeudorAvalista, nombredelacreedoroeldeudoroavalista);

					if (cambiarDatosDescripciondelaoperacion.equals("SI")) {

						String nombredeldeudoroacreedorAvaladoobeneficiarioresidente = SettingsRun.getTestData()
								.getParameter("Nombre del deudor o acreedor / Avalado o beneficiario residente");

						this.write(campInputdescripcionOperacion_NombreDeudorAcredor,
								nombredeldeudoroacreedorAvaladoobeneficiarioresidente);

						String tipodeidentificacióndeldeudor = SettingsRun.getTestData()
								.getParameter("Tipo de identificación del deudor");

						msg = this.seleOption(campSeletListaTipoIdentificacion, tipodeidentificacióndeldeudor);

						if (msg.isEmpty()) {
							Reporter.reportEvent(Reporter.MIC_FAIL,
									" Elemento [" + tipoDestinodelainversion + "] NO presentado");
							this.click(btnEnviar);
//							DXCUtil.wait(5);
							msg = this.closeActiveIntAlertConfirma();
							if (isValid(msg)) {
								return msg;
							}
						}

						String numerodeidentificaciondeldeudor = SettingsRun.getTestData()
								.getParameter("Número de identificación del deudor");
						this.write(campInputNumeroIdentificacion, numerodeidentificaciondeldeudor);

						String digitodeverificacion = SettingsRun.getTestData().getParameter("Dígito de verificación");
						this.write(campInputDigitoVerificacion, digitodeverificacion);

						String monedaestipulada = SettingsRun.getTestData().getParameter("Moneda estipulada");

						msg = this.seleOption(campSeletListaMonedaEstipulada, monedaestipulada);

						if (msg.isEmpty()) {
							Reporter.reportEvent(Reporter.MIC_FAIL,
									" Elemento [" + tipoDestinodelainversion + "] NO presentado");
							this.click(btnEnviar);
							DXCUtil.wait(5);
							msg = this.closeActiveIntAlertConfirma();
							if (isValid(msg)) {
								return msg;
							}
						}

						String valormonedaestipulada = SettingsRun.getTestData()
								.getParameter("Valor moneda estipulada");

						this.write(campInputValorMoneda, valormonedaestipulada);

						String tasadecambiomoneda = SettingsRun.getTestData().getParameter("Tasa de cambio moneda");
						this.write(campInputTasadeCambioMonedaEstipulada, tasadecambiomoneda);

					} else {

						String nombreDeudorAcredor = this.element(campInputdescripcionOperacion_NombreDeudorAcredor).getAttribute("value");

						String deudorAcredorTipoIdentificacion = this.selectListObtenerDatoseleccionado(campSeletListaTipoIdentificacion);

						String deudorAcredorNumeroIdentificacion = this.element(campInputNumeroIdentificacion).getAttribute("value");

						String digitoVerificacion = this.element(campInputDigitoVerificacion).getAttribute("value");

//					String monedaEstipulada = this.selectListObtenerDatoseleccionado(ampSeletListaMonedaEstipulada);

						String monedaEstipulada = this.element(campSeletListaMonedaEstipulada).getText();

						String valorMoneda = this.element(campInputValorMoneda).getAttribute("value");

						String tasadeCambioMonedaEstipulada = this.element(campInputTasadeCambioMonedaEstipulada).getAttribute("value");

					}
				}

				if (cambiarlistnumeralOperacion_Numeral1.equals("SI")) {

					numeral1Acambiar = SettingsRun.getTestData().getParameter("Numeral cambiario A Cambiar 1");

					if (isElementInteractable(listNumeralOperacion_Numeral1)) {
						msg = this.selectListItemExacto(listNumeralOperacion_Numeral1, numeral1Acambiar);
						if (isValid(msg)) {
							return msg;
						}
					}

					if (isElementInteractable(ListNumeralOperacion_Numeral0id)) {
						msg = this.selectListItemExacto(ListNumeralOperacion_Numeral0id, numeral1Acambiar);
						if (isValid(msg)) {
							return msg;
						}
					}
				}
			}

			DXCUtil.wait(3);

			msg = this.closeActiveIntAlertConfirma();

			if (msg != null && msg.contains("Motivo no aplica para este numeral")) {
				Reporter.reportEvent(Reporter.MIC_WARNING, msg + " Se continua con el flujo");
			}

			if (isElementInteractable(inputnumeralOperacion_Participaciones)) {
				this.write(inputnumeralOperacion_Participaciones, "121");
				this.mouseOver(this.element("//label[contains(text(), 'Participaciones')]/following-sibling::div"));
				this.mouseClick();
			}

			DXCUtil.wait(3);
			if (isElementInteractable(seleListnumeralOperacion_MotivosSinParticipacion)) {
				msg = this.selectListItem2(empresaReceptoraCodigoCiudad, "Inversión a Plazos");

				if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
					Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					this.click(btnEnviar);
					msg = this.closeActiveIntAlertConfirma();
					if (isValid(msg)) {
						return msg;
					}
				}
			}

			if (isElementInteractable(inputOperacion_NumeroDeposito)) {

				if (!isValid(numeroDeposito)) {
					Evidence.save("Se presento el Numero Deposito");
					Reporter.reportEvent(Reporter.MIC_INFO,"Se presento el campo Numero Deposito y el campo en la data es nulo o vacio ");
				}

				contador = 0;

				do {
					contador++;
					DXCUtil.wait(1);
					if (contador >= 30) {
						Evidence.save("DYF-Envio");
						this.click(btnEnviar);
						DXCUtil.wait(1);

						msg = this.closeActiveIntAlertConfirma();

						if (isValid(msg))
							Reporter.reportEvent(Reporter.MIC_FAIL, msg);

//						DXCUtil.wait(1);
						msg = this.closeActiveIntAlertConfirma();
						if (isValid(msg))
							this.getDriver().switchTo().defaultContent();

						return msg;

					}
				} while (this.element(inputOperacion_NumeroDeposito) == null
						&& this.isEnabled(inputOperacion_NumeroDeposito));

				this.write(inputOperacion_NumeroDeposito, numeroDeposito);

				Evidence.save("NumeroDeposito");
			}

			if (conceptosCambiario.contains(Exportaciones)) {
				ValidacionDeduciones(numeroDeposito, numeroFacturaoReferDeclaracion, valordeduciones, valorTx);

			} else if (!conceptosCambiario.contains(Exportaciones) || !conceptosCambiario.contains(Inversiones)) {

				double valortx = Double.parseDouble(valorTx.replace(".", "").replace(",", "."));
				double valorenUSD_Precargado = 0.0;

				if (!isValid(valorNumeral1)) {
					if (isElementInteractable(inputValorenUSD))
						valorenUSD_Precargado = ValidarValorNumeralvsPortal(inputValorenUSD, valorTx);

				} else if (isValid(valorNumeral1)) {
					if (isElementInteractable(inputValorenUSD))
						valorenUSD_Precargado = ValidarValorNumeralvsPortal(inputValorenUSD, valorNumeral1);
				}

				// Validacion Numeral
				// 1-----------------------------------------------------------------------------------

				String cambiarValornumeralcambiario1 = SettingsRun.getTestData()
						.getParameter("Cambiar Valor numeral cambiario 1");

				if (cambiarValornumeralcambiario1.equals("SI")) {

					String ValorNumeral1Camb = SettingsRun.getTestData()
							.getParameter("Valor numeral cambiario A Cambiar 1");

					if (isElementInteractable(inputValorMonedaGiro))
						this.write(inputValorMonedaGiro, String.valueOf(ValorNumeral1Camb).replace(".", ","));

					if (isElementInteractable(inputValorMonedaNegociacion))
						this.write(inputValorMonedaNegociacion, String.valueOf(ValorNumeral1Camb).replace(".", ","));

					Reporter.reportEvent(Reporter.MIC_PASS,
							"Se agrega el valor de las deduciones en el valor de la moneda: " + valorenUSD_Precargado);
				}

				// Validacion Numeral
				// 2-----------------------------------------------------------------------------------
				String CambiarNumeral2 = "";

				if (SettingsRun.getTestData().parameterExist("Cambiar Numeral cambiario 2"))
					CambiarNumeral2 = SettingsRun.getTestData().getParameter("Cambiar Numeral cambiario 2");

				String Numeralcambiario2aCambiar = SettingsRun.getTestData()
						.getParameter("Numeral cambiario A Cambiar 2");
				String cambiarValornumeralcambiario2 = SettingsRun.getTestData()
						.getParameter("Cambiar Valor numeral cambiario 2");

				if (isValid(numCambiario2)) {

					if (isElementInteractable(ListNumeralOperacion_Numeral1id))
						elementoSelecionado = ValidacionListaNumerales(ListNumeralOperacion_Numeral1id);

					if (elementoSelecionado.equals("No se presento el campo Numeral")
							|| elementoSelecionado.contains("Error mensaje:")) {
						return elementoSelecionado;
					}

					if (!isValid(elementoSelecionado)
							|| elementoSelecionado.equals("No hay ninguna opción seleccionada.")
							|| elementoSelecionado.equals("Seleccione un numeral")) {

						if (isElementInteractable(ListNumeralOperacion_Numeral1id)) {
							msg = this.selectListItemExacto(ListNumeralOperacion_Numeral1id, numCambiario2);
							if (isValid(msg)) {
								return msg;
							}

							Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						}

					} else {

						Reporter.reportEvent(Reporter.MIC_INFO, "Valor ya seleccionado: " + elementoSelecionado + "");

					}

					double valorenUSD_Precargado2 = ValidarValorNumeralvsPortal(inputValorenUSD1, valorNumeral2);

					if (CambiarNumeral2.equals("SI")) {

						if (isElementInteractable(ListNumeralOperacion_Numeral1id)) {

							msg = this.selectListItemExacto(ListNumeralOperacion_Numeral1id, Numeralcambiario2aCambiar);

							if (isValid(msg)) {
								return msg;
							}

						}

						String ValorNumeral2Camb = SettingsRun.getTestData()
								.getParameter("Valor numeral cambiario A Cambiar 2");

						if (isElementInteractable(inputValorMonedaGiro1))
							this.write(inputValorMonedaGiro1, String.valueOf(ValorNumeral2Camb).replace(".", ","));

						if (isElementInteractable(inputValorMonedaNegociacion1))
							this.write(inputValorMonedaNegociacion1,
									String.valueOf(ValorNumeral2Camb).replace(".", ","));
					}

				}

			} else {

				// el campo esta vacio da clic al boton siguiente
				Evidence.save("Campo-Seleccione Numeral");
				this.click(btnEnviar);

				msg = this.closeActiveIntAlertConfirma();

				if (isValid(msg))
					return msg;
			}

			String concepto = SettingsRun.getTestData().getParameter("Concepto de la transferencia A Cambiar");

			if (isValid(concepto) && !concepto.equals("4 - Inversiones internacionales")) {

				if (concepto.contains(Exportaciones))
					ValidacionDeduciones(numeroDeposito, numeroFacturaoReferDeclaracion, valordeduciones, valorTx);

				String validacionAdicionar = SettingsRun.getTestData().getParameter("Validar Numerales");

				if (isValid(validacionAdicionar) && !validacionAdicionar.equals("No Aplica")
						&& !validacionAdicionar.equals("1")) {

					// Numero de deposito
					if (isElementInteractable(inputOperacion_NumeroDeposito))
						this.write(inputOperacion_NumeroDeposito, numeroDeposito);

					ValidarNumeral(Integer.parseInt(validacionAdicionar));

				}
			}

			// ============================================================================================

			Evidence.save("Informacion-Numeral");

			Reporter.reportEvent(Reporter.MIC_PASS, "Se adiciona los numerales y valores en la moneda degiro");
		}

		// -------------------- [Cargue aquí los documentos que soportan este
		// formulario]-------------------

		DXCUtil.wait(5);

		String validacionDian = SettingsRun.getTestData().getParameter("Validacion - DIAN");

		ValidacionDocumentosDIAN(Integer.parseInt(validacionDian));

		DXCUtil.wait(2);

		msg = this.CargaArchivos(cargarArchivo);

		if (isValid(msg))
			return msg;

		Reporter.reportEvent(Reporter.MIC_PASS, "Se adjunta soporter y / o modificacion de los datos");

		Evidence.save("Cargue de Archivos");
		
		this.click(btnEnviar);
				
		contador = 0;
		
		do {
			
			msg = this.getMsgAlertIfExistxPath("//*[@id='myModal']");
			
		} while (!isValid(msg) && contador <= 5);

		if (isValid(msg)) {
			Reporter.reportEvent(Reporter.MIC_PASS, msg);
			DXCUtil.wait(3);
			if (msg.contains("Documentos enviados exitosamente. Davivienda validará la información recibida y en caso de presentar inconsistencias informará vía correo electrónico. Por favor haga seguimiento de su operación en la opción de consultas y verifique el estado de su trámite.")) {
				msg = this.closeActiveIntAlert();
			}
		}
		
		msg = this.closeActiveIntAlert();
		
		if (isValid(msg)) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
		}

		return msg;
	}

// ============================================[findDocumentWithTime]===========================================================================

//	/**
//	 * Encuentra el numero de la Tx con la fecha y Hora si son necesarios
//	 * 
//	 * @param horaconvert
//	 * @return el objeto en contrado o null
//	 * @throws Exception
//	 */
//	public String findDocumentWithTime(String horaconvert) throws Exception {
//		
//		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
//		String beneficiario = SettingsRun.getTestData().getParameter("Ordenante / Nombre del beneficiario en el exterior");
//		
//		String documentoTx = "";
//		String fechayHoraconver = "";
//
//		if (isValid(numAprova))
//			documentoTx = numAprova;
//
//		if (isValid(horaconvert))
//			fechayHoraconver = DXCUtil.convertirHoraSiPM(horaconvert);
//
//		try {
//			DXCUtil.wait(1);
//			
//			String Obje = xpathNumDocumTxCon.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda).replace("DOCID", documentoTx);
//			
//			if (Obje != null && !isValid(documentoTx))
//				numAprova = this.element(xpathNumDocumTxObt.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda)).getText();
//			
//			return Obje;
//			
//		} catch (Exception e) {
//			
//			return null;
//		}
//
//	}

// ============================================[validarAlertDocuc]===========================================================================	

	/**
	 * Validad las alertas del cargue del documento
	 * 
	 * @return alert o null
	 * @throws Exception
	 */
	public String validarAlertDocuc() throws Exception {

		String msg = null;
		msg = this.closeActiveIntAlert();
		if (isValid(msg) && !msg.contains(
				"Los campos que no se presentan en la declaración de cambio serán autocompletados por el aplicativo")) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
		}
		msg = this.getMsgAlertIfExistxPath("//p[1]");
		if (msg == null)
			msg = this.getMsgAlertIfExist("myModal");

		if (isValid(msg)) {
			if (!msg.contains("En este módulo puede visualizar las operaciones que requieren adjuntar soportes")
					&& !msg.contains("Los campos que no se presentan en la declaración")
					&& !msg.contains("Los campos que no se presentan en la declaración")) {
				Evidence.saveAllScreens(msg, this);
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			}

			if (msg.contains("Los campos que no se presentan en la declaración")) {
				this.element("//*[@id='btnmodalaceptar']").click();
			}
		}

		return msg;
	}

// ============================================[ValidacionTablasInformativa]===========================================================================	

	/**
	 * Valida la Tabla informativa
	 * 
	 * @throws Exception
	 */
	public void ValidacionTablasInformativa() throws Exception {
		WebElement tablaDatosTx = null;
		boolean tablaDesple = false;
		// Verificar tablas principales
		String[] tablasPrimarias = { "table1", "table3", "table4" };
		for (String tabla : tablasPrimarias) {
			tablaDatosTx = obtenerTabla(tabInfoDatosTx, tabla);
			if (tablaDatosTx != null) {
				tablaDesple = true;
				break;
			}
		}

		// Verificar tablas secundarias si no se encontró ninguna principal
		if (!tablaDesple) {
			for (String tabla : tablasPrimarias) {
				tablaDatosTx = obtenerTabla(tabInfoDatos2, tabla);
				if (tablaDatosTx != null) {
					tablaDesple = true;
					break;
				}
			}
		}

		// Procesar la tabla en contrada
		if (tablaDatosTx != null) {
			List<WebElement> encabezados = tablaDatosTx.findElements(By.tagName("th"));
			List<WebElement> datos = tablaDatosTx.findElements(By.tagName("td"));

			String[] textosEncabezados = encabezados.stream().map(WebElement::getText).toArray(String[]::new);

			String[] textosDatos = datos.stream().map(WebElement::getText).toArray(String[]::new);

			// Redirigir la salida para capturar el contenido
			ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
			PrintStream originalOut = System.out;
			System.setOut(new PrintStream(salidaCapturada));

			System.out.println("Tabla de Datos:\n");

			// Imprimir encabezados
			System.out.println(String.join("\t", textosEncabezados));

			// Imprimir datos organizados por filas
			int columnas = textosEncabezados.length;
			for (int i = 0; i < textosDatos.length; i++) {
				System.out.print(textosDatos[i] + "\t");
				if ((i + 1) % columnas == 0) {
					System.out.println();
				}
			}
			// Restaurar salida estándar y capturar el texto generado
			System.setOut(originalOut);
			String textoCapturado = salidaCapturada.toString();

			Reporter.reportEvent(Reporter.MIC_INFO, textoCapturado);
		}
	}

// ============================================[ValidacionTablasInformativa]===========================================================================	

	/**
	 * Metodo se en carga de validar o selecionar el Tipo de operacion si es
	 * [Inicial o Devolución]
	 * 
	 * @param conceptosCambiario
	 * @param tipoOperacion
	 * @throws Exception
	 */
	public void ValidacionTipodeoperacion(String conceptosCambiario, String tipoOperacion) throws Exception {

		WebElement webTipoO = null;
		String pseudoElemento = null;

		if (conceptosCambiario.contains(ServTryOtros) || conceptosCambiario.contains(Endeudamiento)
				|| conceptosCambiario.contains(Inversiones)) {

			if (isValid(tipoOperacion)) {

				if (tipoOperacion.equals(Inicial)) {

					webTipoO = this.element(tipoOperacionXpath.replace("OPERACION", Inicial));

				} else if (tipoOperacion.equals(Devolucion)) {

					webTipoO = this.element(tipoOperacionXpath.replace("OPERACION", Devolucion));
				}

				this.mouseOver(webTipoO);

				this.mouseClick();

				pseudoElemento = (String) this.getJse().executeScript(strPseudoElemento, webTipoO);

				if (!pseudoElemento.equals("rgb(255, 0, 0)"))
					Reporter.reportEvent(Reporter.MIC_WARNING, "El camo de Tipo de Operación No selecionado");

			} else {
				Reporter.reportEvent(Reporter.MIC_INFO, "El camo de Tipo de Operación esta en blanco");
			}

		} else if (conceptosCambiario.contains(Importaciones)) {

			webTipoO = this.element(tipoOperacionXpath.replace("OPERACION", Inicial));
			pseudoElemento = (String) this.getJse().executeScript(strPseudoElemento, webTipoO);

			if (!pseudoElemento.equals("rgb(255, 0, 0)"))
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"El camo de Tipo de Operación No esta selecionado" + Inicial + "para" + Importaciones);

		} else if (conceptosCambiario.contains(Exportaciones)) {

			webTipoO = this.element(tipoOperacionXpath.replace("OPERACION", Devolucion));

			pseudoElemento = (String) this.getJse().executeScript(strPseudoElemento, webTipoO);

			if (!pseudoElemento.equals("rgb(255, 0, 0)"))
				Reporter.reportEvent(Reporter.MIC_WARNING,
						"El camo de Tipo de Operación No esta selecionado" + Devolucion + "para" + Exportaciones);
		}
	}

// ============================================[EmpresaReceptoraoAdministrador]===========================================================================

	/**
	 * Este metodo agrega los datos del receptor (Portafolio)
	 * 
	 * @param tipoId tipo de identificacion del recetor
	 * @param numID  numero de identificación del recetor
	 * @param digNo  digito
	 * @param nomRaz
	 * @return Retorna el mensaje de error o null si es pass
	 * @throws Exception
	 */
	public String EmpresaReceptoraoAdministrador(String tipoId, String numID, String digNo, String nomRaz,
			String codigoPais, String codigoDepartamento, String codigoCiudad, String codigoCIUUEmpresas,
			String telefono, String direccion, String correo, String sector, String tipoEmpresa, String naturaleza,
			String superintendencia) throws Exception {

		String msg = null;

		contador = 0;
		do {
			contador++;
			DXCUtil.wait(3);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				return "No se encuentra el campo empresa Receptora";
			}

		} while (this.element(listSeleOpcionempresaReceptoraTipoIdentificacion) == null
				&& !this.element(listSeleOpcionempresaReceptoraTipoIdentificacion).isDisplayed());

		msg = this.selectListItemExacto(listSeleOpcionempresaReceptoraTipoIdentificacion, tipoId);
//		msg = this.selectListItemExacto(listSeleOpcionempresaReceptoraTipoIdentificacion, "NR");

		Evidence.save("Campo-Seleccione Tipo Identificación");
		if (isValid(msg)) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.click(btnEnviar);
			msg = this.closeActiveIntAlertConfirma();
			if (isValid(msg)) {
				return msg;
			}
		}

		DXCUtil.wait(5);

		msg = this.getActiveIntAlert();

		if (msg != null && msg.contains("Advertencia Para continuar debe completar la información solicitada")) {

			msg = this.closeActiveIntAlertConfirma();
		}

		this.write(imputempresaReceptoraNumeroIdentificacion, numID);

		if (isElementInteractable(imputempresaReceptoraDigitoVerificacion)) {
			this.write(imputempresaReceptoraDigitoVerificacion, digNo);
		}

		if (isElementInteractable(imputempresaReceptoraNombreRazonSocial)) {

			this.write(imputempresaReceptoraNombreRazonSocial, nomRaz);
		}

		if (isElementInteractable(empresaReceptoraCodigoPais_NHidden)) {

			// Dato predetivo
//			this.selectPredictiveListItemLi(empresaReceptoraCodigoPais_NHidden, "ESPAÑA");
			msg = this.selectPredictiveListItemLi(empresaReceptoraCodigoPais_NHidden, codigoPais);
			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		if (isElementInteractable(empresaReceptoraCodigoDepartamento_NHidden)) {

			// Dato predetivo
//			this.selectPredictiveListItemLi(empresaReceptoraCodigoDepartamento_NHidden, "25");
			msg = this.selectPredictiveListItemLi(empresaReceptoraCodigoDepartamento_NHidden, codigoDepartamento);
//			msg = this.selectPredictiveListItemLi(empresaReceptoraCodigoDepartamento_NHidden, "Atlántico");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		DXCUtil.wait(2);

		if (isElementInteractable(empresaReceptoraCodigoCiudad)) {

//			msg = this.selectListItem2(empresaReceptoraCodigoCiudad, "Bogotá");
			msg = this.selectListItem2(empresaReceptoraCodigoCiudad, codigoCiudad);
			String[] listapais = this.getListItems(empresaReceptoraCodigoCiudad);

			for (String listapis : listapais) {
				System.out.println(listapis);
			}

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		if (isElementInteractable(empresaReceptoraCodigoCIUU)) {

//			msg = this.selectListItem2(empresaReceptoraCodigoCIUU, "115");
			msg = this.selectListItem2(empresaReceptoraCodigoCIUU, codigoCIUUEmpresas);

			Evidence.save("Campo-Seleccione Tipo Identificación");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		if (isElementInteractable(empresaReceptoraTelefono))

			this.write(empresaReceptoraTelefono, telefono);
//			this.write(empresaReceptoraTelefono, "5121457");

		if (isElementInteractable(empresaReceptoraDireccion))

			this.write(empresaReceptoraDireccion, direccion);
//			this.write(empresaReceptoraDireccion, "CALLE 190 # 75");

		if (isElementInteractable(empresaReceptoraCorreo))
			this.write(empresaReceptoraCorreo, correo);
//		this.write(empresaReceptoraCorreo, "davivi@gmail.com");

		if (isElementInteractable(empresaReceptoraSector)) {

			msg = this.selectListItem2(empresaReceptoraSector, sector);
//			msg = this.selectListItem2(empresaReceptoraSector, "PU");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}
		if (isElementInteractable(empresaReceptoraTipoEmpresa)) {

			msg = this.selectListItem2(empresaReceptoraTipoEmpresa, tipoEmpresa);
//			msg = this.selectListItem2(empresaReceptoraTipoEmpresa, "1");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		DXCUtil.wait(2);

		if (isElementInteractable(selectempresaReceptora_Naturaleza)) {

			msg = this.selectListItem2(selectempresaReceptora_Naturaleza, naturaleza);
//			msg = this.selectListItem2(selectempresaReceptora_Naturaleza, "Persona jurídica");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		if (isElementInteractable(empresaReceptoraSuperintendencia)) {

			msg = this.selectListItem2(empresaReceptoraSuperintendencia, superintendencia);
//			msg = this.selectListItem2(empresaReceptoraSuperintendencia, "1");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}
		return msg;
	}

// ============================================[IdentificacióndelInversionista]===========================================================================

	/**
	 * Identificación del Inversionista (Nacional o extranjero) Metodo se encarga de
	 * ingresar los Datos del tipo de inversion
	 * 
	 * @return
	 * @throws Exception
	 */
	public String IdentificacióndelInversionista(String tipoId, String numID, String digNo, String nomRaz,
			String codigoPais, String codigoDepartamento, String codigoCiudad, String codigoCIUUInversionista,
			String telefono, String direccion, String correo, String sector, String tipoEmpresa, String naturaleza,
			String superintendencia) throws Exception {

		String msg = null;
		contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
			if (contador >= 30) {
				this.getDriver().switchTo().defaultContent();
				return "No se encuentra el campo Opcion inversionista";
			}

		} while (this.element(listSeleOpcioninversionistaTipoIdentificacion) == null
				&& !this.element(listSeleOpcioninversionistaTipoIdentificacion).isDisplayed());

		msg = this.selectListItemExacto(listSeleOpcioninversionistaTipoIdentificacion, tipoId);
//		msg = this.selectListItemExacto(listSeleOpcioninversionistaTipoIdentificacion, "NI");

		Evidence.save("Campo-Seleccione Tipo Identificación");

		if (isValid(msg)) {
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.click(btnEnviar);
			msg = this.closeActiveIntAlertConfirma();
			if (isValid(msg)) {
				return msg;
			}
		}

		this.write(imputinversionistaNumeroIdentificacion, numID);
//		this.write(imputinversionistaNumeroIdentificacion, "1032516437");

		if (isElementInteractable(imputinversionista_DigitoVerificacion)) {

			this.write(imputinversionista_DigitoVerificacion, digNo);
//			this.write(imputinversionista_DigitoVerificacion, "1");
		}

		this.write(imputinversionista_NombreRazonSocial, nomRaz);
//		this.write(imputinversionista_NombreRazonSocial, "davivien");

		if (isElementInteractable(selectinversionista_CodigoPais)) {

			msg = this.selectListItem2(selectinversionista_CodigoPais, codigoPais);
//			msg = this.selectListItem2(selectinversionista_CodigoPais, "CO");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		if (isElementInteractable(imputinversionista_CodigoDepartamento_NHidden)) {
			// Dato predetivo
			msg = this.selectPredictiveListItemLi(imputinversionista_CodigoDepartamento_NHidden, codigoDepartamento);
//		this.selectPredictiveListItemLi(imputinversionista_CodigoDepartamento_NHidden, "Bogotá D.C.");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}
		DXCUtil.wait(5);

		msg = this.getActiveIntAlert();

		if (msg != null && msg.contains("Advertencia Para continuar debe completar la información solicitada")) {
			Reporter.reportEvent(Reporter.MIC_WARNING, msg);
			this.closeActiveIntAlertConfirma();

		}

		if (isElementInteractable(selectinversionista_CodigoCiudad)) {

			msg = this.selectListItem2(selectinversionista_CodigoCiudad, codigoCiudad);
//			msg = this.selectListItem2(selectinversionista_CodigoCiudad, "BOGOTÁ");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		DXCUtil.wait(2);
		// Dato predetivo
		if (isElementInteractable(imputinversionista_CodigoCIUU_NHidden)) {

			msg = this.selectPredictiveListItemLi(imputinversionista_CodigoCIUU_NHidden, codigoCIUUInversionista);
//			this.selectPredictiveListItemLi(imputinversionista_CodigoCIUU_NHidden, "4775");
			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}
		Evidence.save("Datos_Ingresados");

		DXCUtil.wait(2);

		if (isElementInteractable(selectinversionista_Naturaleza)) {

			msg = this.selectListItem2(selectinversionista_Naturaleza, naturaleza);
//			msg = this.selectListItem2(selectinversionista_Naturaleza, "Persona jurídica");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		DXCUtil.wait(2);

		if (isElementInteractable(imputinversionista_Telefono))
			this.write(imputinversionista_Telefono, telefono);
//			this.write(imputinversionista_Telefono, "5121457");

		DXCUtil.wait(1);
		if (isElementInteractable(imputinversionista_Correo))

			this.write(imputinversionista_Correo, correo);
//			this.write(imputinversionista_Correo, "davivi@gmail.com");

		DXCUtil.wait(1);
		if (isElementInteractable(imputinversionista_Direccion))
			this.write(imputinversionista_Direccion, direccion);
//		this.write(imputinversionista_Direccion, "calle 115");

		DXCUtil.wait(1);
		if (isElementInteractable(selectinversionista_Sector)) {

			msg = this.selectListItem2(selectinversionista_Sector, sector);
//			msg = this.selectListItem2(selectinversionista_Sector, "PU");

			if (isValid(msg) && !msg.contains(
					"Valor ya seleccionado [PU = Público] - no es igual a [PU] pero se deja por estar contenido.")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				DXCUtil.wait(3);
				msg = this.closeActiveIntAlertConfirma();
				if (msg == null) {
					msg = this.closeActiveIntAlert();
				}

				if (isValid(msg)) {
					return msg;
				}
			}
		}

		DXCUtil.wait(1);
		if (isElementInteractable(selectinversionista_TipoEmpresa)) {

			msg = this.selectListItem2(selectinversionista_TipoEmpresa, tipoEmpresa);
//			msg = this.selectListItem2(selectinversionista_TipoEmpresa, "1");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}

		DXCUtil.wait(1);

		if (isElementInteractable(selectinversionista_Superintendencia)) {

			msg = this.selectListItem2(selectinversionista_Superintendencia, superintendencia);
//			msg = this.selectListItem2(selectinversionista_Superintendencia, "1");

			if (isValid(msg) && !msg.contains("Valor ya seleccionado")) {
				Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				this.click(btnEnviar);
				msg = this.closeActiveIntAlertConfirma();
				if (isValid(msg)) {
					return msg;
				}
			}
		}
		DXCUtil.wait(2);
		return msg;
	}

// ============================================[ValidacionListaNumeral1]===========================================================================

	/**
	 * 
	 * @param conceptosCambiario
	 * @return
	 * @throws Exception
	 */
	public String ValidacionListaNumeral1(String conceptosCambiario) throws Exception {

		WebElement listnumeralOperacion_Numeral1 = null;
		contador = 0;
		String msg = null;
		String elementoSelecionado = "";
		do {

			DXCUtil.wait(1);

			listnumeralOperacion_Numeral1 = this.element(listNumeralOperacion_Numeral1);

			if (!conceptosCambiario.contains(Inversiones)) {
				DXCUtil.wait(1);
				if (isElementInteractable(ListNumeralOperacion_Numeral0id))
					listnumeralOperacion_Numeral1 = this.element(ListNumeralOperacion_Numeral0id);
			}

			contador++;
			if (contador >= 30) {

				msg = "No se presento el campo Numeral";

				if (this.element(sesionEx) != null) {

					msg = this.element(sesionEx).getText();

					if (isValid(msg)) {
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					}

					Evidence.save("No se presento el campo Numeral");

					if (this.element(btnEnviar) != null) {
						this.click(btnEnviar);
					}

					if (isValid(msg))
						return "Error mensaje: " + msg;
				}

				Evidence.save("No se presento el campo Numeral");

				this.click(btnEnviar);

				return "No se presento el campo Numeral";
			}

			if (listnumeralOperacion_Numeral1 != null) {
				if (isElementInteractable(listnumeralOperacion_Numeral1)) {
					elementoSelecionado = this.selectListObtenerDatoseleccionado(listnumeralOperacion_Numeral1);
				}
			}

		} while (this.element(listnumeralOperacion_Numeral1) == null
				|| this.isEnabled(listnumeralOperacion_Numeral1) == false
						&& this.isDisplayed(listnumeralOperacion_Numeral1) == false);

		return elementoSelecionado;
	}

// ============================================[ValidacionListaNumerales]===========================================================================	

	public String ValidacionListaNumerales(By locator) throws Exception {

		WebElement listnumeralOperacion_Numerales = null;
		contador = 0;
		String msg = null;
		String elementoSelecionado = "";
		do {
			DXCUtil.wait(1);
			listnumeralOperacion_Numerales = this.element(locator);
			contador++;
			if (contador >= 30) {
				msg = "No se presento el campo Numeral";
				if (this.element(sesionEx) != null) {

					msg = this.element(sesionEx).getText();

					if (isValid(msg)) {
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
					}

					Evidence.save("No se presento el campo Numeral");

					if (this.element(btnEnviar) != null) {
						this.click(btnEnviar);
					}

					if (isValid(msg))
						return "Error mensaje: " + msg;
				}

				Evidence.save("No se presento el campo Numeral");
				this.click(btnEnviar);
				return "No se presento el campo Numeral";
			}

			if (isElementInteractable(listnumeralOperacion_Numerales)) {
				elementoSelecionado = this.selectListObtenerDatoseleccionado(listnumeralOperacion_Numerales);
			}

		} while (this.element(listnumeralOperacion_Numerales) == null
				|| this.isEnabled(listnumeralOperacion_Numerales) == false
						&& this.isDisplayed(listnumeralOperacion_Numerales) == false);

		return elementoSelecionado;
	}

// ============================================[ValidarValorNumeralvsPortal]===========================================================================	

	public double ValidarValorNumeralvsPortal(By locator, String valornumeral) throws Exception {

		double valorenUSD_Precargado = Double.parseDouble(this.getText(locator).replace(".", "").replace(",", "."));
		double valorNumeraldata = Double.parseDouble(valornumeral);

		if (valorNumeraldata == valorenUSD_Precargado) {
			Reporter.reportEvent(Reporter.MIC_PASS,
					"El valor de la Moneda corresponde con el Mostrado en el portal: " + valorenUSD_Precargado);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL,
					"El valor de la Moneda NO corresponde con el Mostrado en el portal: " + valorenUSD_Precargado);
		}
		return valorenUSD_Precargado;
	}

// ============================================[ValidarNumeral]===========================================================================

	/**
	 * Valida los numerales a Adicionar
	 * 
	 * @param num
	 * @throws Exception
	 */
	public void ValidarNumeral(int num) throws Exception {
		int contadorVa = 1;
		for (int i = 2; i <= num; i++) {
			contadorVa++;
			String cambiarNumeralN = "";
			if (SettingsRun.getTestData().parameterExist("Cambiar Numeral cambiario " + i))
				cambiarNumeralN = SettingsRun.getTestData().getParameter("Cambiar Numeral cambiario " + i);

			if (cambiarNumeralN.equals("Adicionar numeral")) {

				// locator Lista Numeral y valor Numral
				String listNumeralId = ListNumeralOperacion_Numeral2id.replace("NUM", Integer.toString((i - 1)));

				String inputValorMonedaId = inputValorMonedaGiro2.replace("NUM", Integer.toString((i - 1)));
				String inputValorMonedaIdNegociacion = inputValorMonedaNegociacion2.replace("NUM",
						Integer.toString((i - 1)));
				String inputValorMonedaValorenUSD = inputValorenUSD2.replace("NUM", Integer.toString((i - 1)));

				String inputNumeroDeclaracion = inputNumeroDeclaracion2.replace("NUM", Integer.toString((i - 1)));
				String inputNumeroDeposito = inputOperacion_NumeroDeposito2.replace("NUM", Integer.toString((i - 1)));

				String inputValorMonedaEstipulada = inputValorMonedaEstipulada2.replace("NUM",
						Integer.toString((i - 1)));

				// Datos a agregar o valor a cambiar
				String numeralACambiar = SettingsRun.getTestData().getParameter("Numeral cambiario A Cambiar " + i);
				String valorNumeralCambiar = SettingsRun.getTestData()
						.getParameter("Valor numeral cambiario A Cambiar " + i);

				String valorDeclaracio = SettingsRun.getTestData().getParameter("Número de declaración " + i);
				String ValorMonedaEstipulada = SettingsRun.getTestData().getParameter("Valor moneda estipulada " + i);
				String valorDeposito = SettingsRun.getTestData().getParameter("Número de depósito " + i);

				String result = AdicionarNumeral(contadorVa, listNumeralId, inputValorMonedaId,
						inputValorMonedaIdNegociacion, inputValorMonedaValorenUSD, inputNumeroDeclaracion,
						inputNumeroDeposito, numeralACambiar, valorNumeralCambiar, valorDeclaracio, valorDeposito,
						inputValorMonedaEstipulada, ValorMonedaEstipulada);

				if (isValid(result)) {
					Reporter.reportEvent(Reporter.MIC_INFO, "Error en la iteración " + i + ": " + result);
					break; // Detener el ciclo si hay un error
				}
			}
		}
	}

//==============================================================================================================================================================================================

// ===========================================[AdicionarNumeral]===========================================================================
	/**
	 * Adiciona la informacion del numeral dependiendo si tiene valor numero de
	 * posito o informacion
	 * 
	 * @param listNumeralId
	 * @param inputValorMonedaId
	 * @param inputValorMonedaIdNegociacion
	 * @param inputValorMonedaValorenUSD
	 * @param numeralACambiar
	 * @param valorNumeralCambiar
	 * @return
	 * @throws Exception
	 */
	public String AdicionarNumeral(int contadorVa, String listNumeralId, String inputValorMonedaId,
			String inputValorMonedaIdNegociacion, String inputValorMonedaValorenUSD, String inputNumeroDeclaracio,
			String inputNumeroDeposito, String numeralACambiar, String valorNumeralaCambiar, String valorDeclaracio,
			String valorDeposito, String inputValorMonedaEstipulada, String ValorMonedaEstipulada) throws Exception {

		String elementoSelecionado = "";
		String msg = "";

		String validacionAdicionar = SettingsRun.getTestData().getParameter("Validar Numerales");

		if (contadorVa <= Integer.parseInt(validacionAdicionar)) {

			// Adicionar nuevos campos numeral
			this.click(By.xpath("//*[@id='addNewCardBtn']"));
		}

		DXCUtil.wait(2);

		// Dato numeral
		elementoSelecionado = ValidacionListaNumerales(By.id(listNumeralId));
		if (elementoSelecionado.equals("No se presento el campo Numeral")
				|| elementoSelecionado.contains("Error mensaje:")) {
			return elementoSelecionado;
		}

		if (!isValid(elementoSelecionado) || elementoSelecionado.equals("No hay ninguna opción seleccionada.")
				|| elementoSelecionado.equals("Seleccione un numeral")) {

			if (this.element(By.id(listNumeralId)) != null) {
				msg = this.selectListItemExacto(this.element(By.id(listNumeralId)), numeralACambiar);

				if (isValid(msg)) {
					return msg;
				}
			}

		} else {

			Reporter.reportEvent(Reporter.MIC_INFO, "Valor ya seleccionado: " + elementoSelecionado + "");
			msg = "Valor ya seleccionado: " + elementoSelecionado + "";

		}

		DXCUtil.wait(2);

		// Dato valor del numeral
		if (isElementInteractable(By.id(inputValorMonedaId))) {
			this.write(this.element(By.id(inputValorMonedaId)), String.valueOf(valorNumeralaCambiar).replace(".", ","));

		} else if (isElementInteractable(By.id(inputValorMonedaIdNegociacion))) {

			this.write(this.element(By.id(inputValorMonedaIdNegociacion)),
					String.valueOf(valorNumeralaCambiar).replace(".", ","));

		} else if (isElementInteractable(By.id(inputValorMonedaValorenUSD)))
			this.write(this.element(By.id(inputValorMonedaValorenUSD)),
					String.valueOf(valorNumeralaCambiar).replace(".", ","));

		// Dato Numero de Declaracion
		if (isElementInteractable(By.id(inputNumeroDeclaracio)))
			this.write(this.element(By.id(inputNumeroDeclaracio)), valorDeclaracio);

		// Dato Numero de Deposito
		if (isElementInteractable(By.id(inputNumeroDeposito)))
			this.write(this.element(By.id(inputNumeroDeposito)), valorDeposito);

		// Dato Valor moneda estipulada
		if (isElementInteractable(By.id(inputValorMonedaEstipulada)))
			this.write(this.element(By.id(inputValorMonedaEstipulada)), ValorMonedaEstipulada);

		return msg;

	}

// ===========================================[ValidacionDeduciones]===========================================================================

	public void ValidacionDeduciones(String numeroDeposito, String numeroFacturaoReferDeclaracion,
			String valordeduciones, String valorTx) throws Exception {

		// Numero de deposito
		DXCUtil.wait(3);
		if (isElementInteractable(inputOperacion_NumeroDeposito))
			this.write(inputOperacion_NumeroDeposito, numeroDeposito);

		if (isElementInteractable(inputNumeroDeclaracion)) {

			if (!isValid(numeroFacturaoReferDeclaracion)) {
				Evidence.save("NumeroDeclaracion");
				Reporter.reportEvent(Reporter.MIC_INFO, "Celda de Numero Declaracion vacio");

			} else {

				this.write(inputNumeroDeclaracion, numeroFacturaoReferDeclaracion);
			}

			Evidence.save("NumeroDeclaracion");

		} else {

			Reporter.reportEvent(Reporter.MIC_INFO, "No aparecio el campo Declaracion");
		}

		if (isElementInteractable(inputnumeralExtendidoDeducciones)) {

			if (isValid(valordeduciones)) {

				Evidence.save("Numeral Declaracion");
				Reporter.reportEvent(Reporter.MIC_INFO, "Celda de Deducciones vacio");

			} else {

				// Interatua priemro en el campo label
				this.mouseOver(labelDeduciones);
				this.mouseClick();
				this.mouseClick();

				// Realiza la acion tap para selecionar el campo de deduciones
				DXCUtil.BonotesTecla("TAB");
				DXCUtil.wait(4);

				// Escribe el valor de deducion
				DXCUtil.StringNumerosTeclado(valordeduciones);
				DXCUtil.wait(4);
				// espera que se ingresen los datos

				// Interatua nuevamente en el campo label
				this.mouseOver(labelDeduciones);
				this.mouseClick();

				// Espera la alerta si esiste
				String msg = this.closeActiveIntAlert();

				if (isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}

				double valorenUSD_Precargado = Double
						.parseDouble(this.getText(inputValorenUSD).replace(".", "").replace(",", "."));
				double valorDeducionesdata = Double.parseDouble(valordeduciones.replace(".", "").replace(",", "."));
				double valortx = Double.parseDouble(valorTx.replace(".", "").replace(",", "."));

				if (valortx == valorenUSD_Precargado) {
					Reporter.reportEvent(Reporter.MIC_PASS,
							"El valor de la Moneda corresponde con el Cargado en el portal: " + valorenUSD_Precargado);
				}

				double deducionesS = (valorDeducionesdata + valorenUSD_Precargado);

				this.write(inputValorenUSD, String.valueOf(deducionesS).replace(".", ","));

				Reporter.reportEvent(Reporter.MIC_PASS,
						"Se agrega el valor de las deduciones en el valor de la moneda: " + valorenUSD_Precargado);
			}

			Evidence.save("Numeral ExtendidoDeducciones");

		} else {

			Reporter.reportEvent(Reporter.MIC_INFO, "No aparecio el campo Deducciones");
		}
	}

// ===========================================[ValidacionDocumentosDIAN]===========================================================================

	/**
	 * Adiciona informacion de la Dian
	 * 
	 * @param num
	 * @throws Exception
	 */
	public void ValidacionDocumentosDIAN(int num) throws Exception {

		int contadorVa = 1;
		for (int i = 0; i <= num; i++) {
			String listDeclaración = "";
			String ValorDeclaraciónUSD = "";
			contadorVa++;

			if (SettingsRun.getTestData().parameterExist("Declaración de importación No " + (1 + i)))
				listDeclaración = SettingsRun.getTestData().getParameter("Declaración de importación No " + (1 + i));

			if (SettingsRun.getTestData().parameterExist("Valor en USD - DIAN " + (1 + i)))
				ValorDeclaraciónUSD = SettingsRun.getTestData().getParameter("Valor en USD - DIAN " + (i + 1));

			// locator Lista Numeral y valor Numral
			String listDeclaracióndeimportaciónNo = inputlisDeclaraciondeimportacionNo.replace("NUM",
					Integer.toString(i));
			String inputValorenUSB = inputValorUSD.replace("NUM", Integer.toString(i));

			String result = AdicionarDocDIAN(listDeclaracióndeimportaciónNo, inputValorenUSB, listDeclaración,
					ValorDeclaraciónUSD);

			if (isValid(result)) {
				Reporter.reportEvent(Reporter.MIC_INFO, "Error en la iteración " + i + ": " + result);
				break; // Detener el ciclo si hay un error
			}

			String validacionDian = SettingsRun.getTestData().getParameter("Validacion - DIAN");
			// Adicionar nuevos campos numeral
			if (contadorVa <= Integer.parseInt(validacionDian)) {
				this.click(By.xpath("//*[@id='addBtn']"));
			}

		}
	}

// ===========================================[AdicionarDocDIAN]===========================================================================

	public String AdicionarDocDIAN(String listDeclaracióndeimportaciónNo, String inputValorenUSB,
			String importaCionNumero, String valorUSD) throws Exception {

		String msg = "";

		if (isElementInteractable(By.id(listDeclaracióndeimportaciónNo))) {
			Reporter.reportEvent(Reporter.MIC_INFO, "Información documentos de importación - DIAN");
//			this.write(this.element(listDeclaracióndeimportaciónNo), String.valueOf(DXCUtil.enteroRandom(1, 100)));
			this.write(By.id(listDeclaracióndeimportaciónNo), importaCionNumero);

			if (this.element(By.id(inputValorenUSB)) != null && this.isDisplayed(By.id(inputValorenUSB))) {
				this.write(By.id(inputValorenUSB), valorUSD);
			}
		} else {
			msg = "No se agrego informacion";
		}
		return msg;

	}

// ===========================================[CargaArchivos]===========================================================================

	/**
	 * Realizar el cargue de Archivos en el Modulo Documen_Y_Formularios
	 * 
	 * @param cargarArchivo
	 * @return
	 * @throws Exception
	 */
	public String CargaArchivos(String... cargarArchivo) throws Exception {
		contador = 0;
		do {
			contador++;
			DXCUtil.wait(3);
			if (contador >= 30) {

				this.getDriver().switchTo().defaultContent();
				return "No se encuentra el boton de cargar archivo";

			}
		} while (this.element(btnSeleccioneunarchivo) == null && this.isDisplayed(btnSeleccioneunarchivo) == false);

		contador = 0;
		String msg = null;

		for (String archivo : cargarArchivo) {
			// el campo esta vacio da clic al boton siguiente
			Evidence.save("Cargue archivo");
			contador++;
			if (contador != 1) {
				DXCUtil.wait(1);
			}
			this.focus(btnSeleccioneunarchivo);
			this.mouseOver(btnSeleccioneunarchivo);
			this.mouseClick();
			String scriptClicIngresar1 = "document.querySelector(\"#uploadBtn\").click()";
			this.getJse().executeScript(scriptClicIngresar1);
			DXCUtil.wait(3);
			DXCUtil.cargueArchivo(archivo);

			WebElement webArchtrue = null;
			contador = 0;
			String nombreArch = "";
			do {
				DXCUtil.wait(1);
				msg = this.closeActiveIntAlert();
				if (isValid(msg)) {
					return msg;
				}
				String[] rutaArch = archivo.split("\\\\");
				nombreArch = rutaArch[rutaArch.length - 1];
				webArchtrue = this.element(archivoCargado.replace("NOMARCH", nombreArch));
				contador++;
			} while (this.element(webArchtrue) == null && !this.isDisplayed(webArchtrue) && contador <= 29);
			Reporter.reportEvent(Reporter.MIC_PASS, "Se carga documento: " + nombreArch);
		}
		return msg;
	}

// ===========================================[obtenerTabla]===========================================================================

	// Método auxiliar para verificar y obtener la tabla
	private WebElement obtenerTabla(String identificador, String reemplazo) {
		WebElement tabla = this.element(identificador.replace("TABLENUM", reemplazo));
		if (tabla != null && tabla.isDisplayed()) {
			return tabla;
		}
		return null;
	}

// ===========================================[getTipoIdentificaión]===========================================================================

	private String getTipoCuenta(String tipoIdentifiacionComp) {

		String tipoIdentificacion = tipoIdentifiacionComp;

		if (tipoIdentifiacionComp.equals("CÉDULA DE CIUDADANÍA")) {
			tipoIdentificacion = "CC";
		} else if (tipoIdentifiacionComp.equals("CÉDULA DE EXTRANJERÍA")) {
			tipoIdentificacion = "CE";
		} else if (tipoIdentifiacionComp.equals("NIT")) {
			tipoIdentificacion = "NI";
		} else if (tipoIdentifiacionComp.equals("RECEPTOR EN CONSTITUCIÓN")) {
			tipoIdentificacion = "REC";
		} else if (tipoIdentifiacionComp.equals("PATRIMONIO AUTÓNOMO")) {
			tipoIdentificacion = "PAC";
		} else if (tipoIdentifiacionComp.equals("PASAPORTE")) {
			tipoIdentificacion = "PB";
		} else if (tipoIdentifiacionComp.equals("NO RESIDENTE")) {
			tipoIdentificacion = "NR";
		}

		return tipoIdentificacion;
	}
	// ===========================================[Sector]===========================================================================

	private String getSector(String sector) {
		String tipoIdentificacion = sector;
		if (sector.equals("Público")) {
			tipoIdentificacion = "PU";
		} else if (sector.equals("Privado")) {
			tipoIdentificacion = "PR";
		} else if (sector.equals("Mixto")) {
			tipoIdentificacion = "MX";
		}
		return tipoIdentificacion;
	}

	// ===========================================[Superintendencia]===========================================================================

	private String getSuperintendencia(String sector) {
		String sectorDeVigilancia = sector;
		if (sector.equals("1. Superintendencia de Sociedades")) {
			sectorDeVigilancia = "1";
		} else if (sector.equals("2. Superintendencia Financiera de Colombia")) {
			sectorDeVigilancia = "2";
		} else if (sector.equals("3. Superintendencia de Puertos y Transporte")) {
			sectorDeVigilancia = "3";
		} else if (sector.equals("4. Superintendencia de la Economía Solidaria")) {
			sectorDeVigilancia = "4";
		} else if (sector.equals("5. Superintendencia de Vigilancia y Seguridad Privada")) {
			sectorDeVigilancia = "5";
		} else if (sector.equals("6. Superintendencia de Industria y Comercio")) {
			sectorDeVigilancia = "6";
		} else if (sector.equals("7. Superintendencia del Subsidio Familiar")) {
			sectorDeVigilancia = "7";
		} else if (sector.equals("8. Superintendencia Nacional de Salud")) {
			sectorDeVigilancia = "8";
		} else if (sector.equals("9. Superintendencia de Servicios Públicos Domiciliarios")) {
			sectorDeVigilancia = "9";
		}
		return sectorDeVigilancia;
	}

	// ===========================================[TipoEmpresa]===========================================================================

	private String getTipoEmpresa(String sector) {
		String sectorDeVigilancia = sector;
		if (sector.equals("1. Sociedades / Empresas unipersonales")) {
			sectorDeVigilancia = "1";
		} else if (sector.equals("2. Entidades de naturaleza cooperativa")) {
			sectorDeVigilancia = "2";
		} else if (sector.equals("3. Entidad sin ánimo de lucro")) {
			sectorDeVigilancia = "3";
		} else if (sector.equals("4. Entidades gubernamentales del orden nacional o territorial")) {
			sectorDeVigilancia = "4";
		}
		return sectorDeVigilancia;
	}
}
