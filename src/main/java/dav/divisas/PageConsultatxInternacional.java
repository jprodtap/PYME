package dav.divisas;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import dav.c360.PageInicioC360;
import dav.c360.PageLogin;
import dav.c360.moduloPersonas.PageEmpresas;
import dav.c360.moduloPersonas.PagePersona;
import dav.transversal.DatosDavivienda;
import dav.transversal.MovimientoStratus;
import dav.transversal.Stratus;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dav.pymes.moduloCrearTx.ControllerCrearTx;

import dxc.util.DXCUtil;

public class PageConsultatxInternacional extends PageDivisas {

//	static String numAprova = null;
	
	int noDocum;
	int contador = 0;

// ***********************************************************************************************************************

	static String saldoTotalInicial = null;
	static String saldoDisInicial = null;
	static String saldoTotalFinal = null;
	static String saldoDisponibleFinal = null;
	static String DateFechaTx = null;

// ***********************************************************************************************************************

	By btnMenuconsultas = By.xpath("/html/body/div[1]/div[2]/nav/ul/li[4]");

	String filaLocator;
	String xpathdocumentoTx = "//*[@id='paginacion']/div[2]/div[3]/table/tbody/tr[INUMERO]/td/a[contains(text(),'DocumentoTx')]";

	String filaLocatorCont;
	String xpathfilaLocatorCont = "//*[@id='paginacion']/div[2]/div[3]/table/tbody/tr[I]/td[J]";

	By CerrSesion = By.xpath("//*[@id='CerrarSesion']");
	ControllerCrearTx controller = null;
	PageInformesTransInternacionales informes = null;

	// Validacion de los campos solicitados en el set de pruebas
	By title = By.xpath("/html/body/div[1]/div[1]/div/h3");
	By subtitleConsulta = By.xpath("/html/body/div[2]/div[1]/div/h2/string");
	By subtitleParametros = By.xpath("//*[@id='paginacion']/div[1]/div[1]/div/h3");
	By campoDocumento = By.xpath("//*[@id='Documento']");
	By campoTipoTrans = By.xpath("//*[@id='TipoTransferencia']");
	By campoOrdeBene = By.xpath("//*[@id='OrdenanteBeneficiario']");
	By campoEstado = By.xpath("//*[@id='Estado']");
	By campoMoneda = By.xpath("//*[@id='Moneda']");
	By btnDescargar = By.xpath("//*[@id='paginacion']/div[4]/div/button");

// ******************************[FILTROS]*****************************************************************************************

	String cmpimputTipoConsulta = "//label[contains(text(),'TIPODCON')]//following-sibling::span//input";

	By cmpimputFechaDesde = By.id("FechaDesde");

	By cmpimputHasta = By.id("FechaHasta");

	By cmpimputTipoTransferencia = By.id("TipoTransferencia");

	By cmpimputOrdenanteBeneficiario = By.id("OrdenanteBeneficiario");
	By cmpimputDocumento = By.id("Documento");

	By cmpimputEstado = By.id("Estado");
	By cmpimputMoneda = By.id("Moneda");

	By btnBuscarxpath = By.xpath("//*[@id='paginacion']/div[1]/div[9]/button");

	
// ***********************************************************************************************************************
	
	String seleccionLinkDocumentoXpath = "//*[@id='paginacion']/div[2]/div[3]/table/tbody/tr[noDocum]/td[2]/a[contains(text(),'documento')]";

	String headerCompLocator = "//*[@class='clearfix'][I]/div[1]";
	String dataCompLocator = "//*[@class='clearfix'][I]/div[2]";

//	String xpathNumDocumTxCon = "//td[contains(text(), 'fechayhoraconvert')]/following-sibling::td[contains(text(), 'MONEDA')]/preceding-sibling::td[4]";
	
	String xpathBuscarFechayHora = "//td[contains(text(), 'fechayhoraconvert')]";

	String aler = "//*[@id='AlertaModal']/div/div/div[2]/p[contains(text(), 'No existen resultados para esta búsqueda')]";
	String sesionexp = "/html/body/div/div/div/b[contains(text(), 'Sesión no existe o ha expirado por inactividad.')]";

	By cmpValorTotalTx = By.xpath("//label[contains(text(), 'Valor total descontado de la cuenta:')]/following::div[1]");
	By cmpfechaTx = By.xpath("//label[contains(text(), 'Fecha')]/following::div[1]");

	PageLogin pageLoginC360 = null;
	PageInicioC360 pageInicioC360 = null;
	PageEmpresas pageEmpresasC360 = null;

	public PageConsultatxInternacional(BasePageWeb parentPage) {
		super(parentPage);
		this.pageInicioC360 = new PageInicioC360(parentPage);
		this.pageEmpresasC360 = new PageEmpresas(parentPage);
	}
	

// ***********************************************************************************************************************

	public void setSaldoTotalInicial(String saldo) {
		this.saldoTotalInicial = saldo;
	}

	public void setSaldoDisInicial(String saldo) {
		this.saldoDisInicial = saldo;
	}

	public void setSaldoTotalFinal(String saldo) {
		this.saldoTotalFinal = saldo;
	}

	public void setSaldoDisponibleFinal(String saldo) {
		this.saldoDisponibleFinal = saldo;
	}

// ============================================[ConsultaNumtx]===========================================================================

	/**
	 * Obtiene el numero de aprobacion de la tx
	 * 
	 * @throws Exception
	 */
	public void ConsultaNumtx(String servicio) throws Exception {
		
		String documentoTx = numAprova;
		
		String filaArray[] = new String[8];

		this.InicioConsulta(servicio);

		this.ErrorSesionExpirada();

		// Busca por el numero de aprobacion y guarda en un array los datos del
		// registro, si lo encuentra
//		if (!isValid(documentoTx)) {
//
//			if (SettingsRun.getTestData().parameterExist("Fecha tx") && SettingsRun.getTestData().parameterExist("Hora tx")) {
//
//				String fecha = SettingsRun.getTestData().getParameter("Fecha tx").trim();
//				String hora = SettingsRun.getTestData().getParameter("Hora tx").trim();
//
//				// Intenta encontrar el documento con la hora original
//				if (isValid(fecha) && isValid(hora)) {
//
//					// Buscar botón "Siguiente"
//					WebElement btnSiguiente = null;
//
//					btnSiguiente = this.element(By.xpath("//button[contains(text(),'Siguiente')]"));
//
//					if (isElementInteractable(btnSiguiente)) {
//
//						WebElement paginaElement = this.element(By.xpath("//*[@id='pagina']"));
//
//						// Obtiene la opción seleccionada
//						String paginaActualStr = null;
//						int paginaActual = 0;
//						int totalPaginas = 0;
//						Select dropdown = null;
//						String[] paginas = null;
//
//						// Crea un objeto Select para manipularlo
//						if (paginaElement != null) {
//							dropdown = new Select(paginaElement);
//							paginaActualStr = dropdown.getFirstSelectedOption().getText().trim();
//						}
//
//						if (isValid(paginaActualStr)) {
//							paginas = paginaActualStr.split("de");
//							paginaActual = Integer.parseInt(paginas[0].trim());
//							totalPaginas = Integer.parseInt(paginas[1].trim());
//						}
//
//						do {
//
//							btnSiguiente = this.element(By.xpath("//button[contains(text(),'Siguiente')]"));
//
//							// Buscar la fecha y hora en la página actual
//							List<WebElement> listaFecha = null;
//							List<WebElement> listahora = null;
//							listaFecha = this.findElements(By.xpath(xpathBuscarFechayHora.replace("fechayhoraconvert", fecha)));
//							listahora = this.findElements(By.xpath(xpathBuscarFechayHora.replace("fechayhoraconvert", hora)));
//
//							// Si encuentra ambos, intenta obtener el documento
//							if (listaFecha != null && listahora != null) {
//								documentoTx = findDocumentWithTimeAfterDelay(fecha, hora);
//								if (isValid(documentoTx)) {
//									break; // Documento encontrado
//								}
//							}
//
//							paginaElement = this.element(By.xpath("//*[@id='pagina']"));
//							// Crea un objeto Select para manipularlo
//							dropdown = new Select(paginaElement);
//
//							// Obtiene la opción seleccionada
//							paginaActualStr = dropdown.getFirstSelectedOption().getText().trim();
//							paginas = paginaActualStr.split("de");
//
//							paginaActual = Integer.parseInt(paginas[0].trim());
//
//							// Verificamos si ya estamos en la última página
//							if (paginaActual >= totalPaginas) {
//								break;
//							}
//
//							// Verificar si el botón siguiente está disponible
//							btnSiguiente = this.element(By.xpath("//button[contains(text(),'Siguiente')]"));
//
//							if (isElementInteractable(btnSiguiente)) {
//								this.click(btnSiguiente);
//								DXCUtil.wait(10); // Espera a que cargue la siguiente página
//							} else {
//								break;
//							}
//						} while (isElementInteractable(btnSiguiente));
//					}
//
//					documentoTx = findDocumentWithTimeAfterDelay(fecha, hora); // Esperar un minuto
//
//					if (!isValid(documentoTx)) {
//						// Si no lo encuentra, resta un minuto y vuelve a intentarlo
//						String modifiedHora1 = DXCUtil.subtractOneMinute(hora);
//						documentoTx = findDocumentWithTime(fecha, modifiedHora1);
//						if (!isValid(documentoTx)) {
//							// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//							String modifiedHora2 = DXCUtil.subtractOneMinute(modifiedHora1);
//							documentoTx = findDocumentWithTime(fecha, modifiedHora2);
//							if (!isValid(documentoTx)) {
//								// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//								String modifiedHora3 = DXCUtil.subtractOneMinute(modifiedHora2);
//								documentoTx = findDocumentWithTime(fecha, modifiedHora3);
//								if (!isValid(documentoTx)) {
//									// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//									String modifiedHora4 = DXCUtil.subtractOneMinute(modifiedHora3);
//									documentoTx = findDocumentWithTime(fecha, modifiedHora4);
//									if (!isValid(documentoTx)) {
//										// Si aún no lo encuentra, resta un minuto y vuelve a intentarlo
//										String modifiedHora5 = DXCUtil.subtractOneMinute(modifiedHora4);
//										documentoTx = findDocumentWithTime(fecha, modifiedHora5);
//										if (!isValid(documentoTx)) {
//											// Si no se encuentra incluso después de los ajustes, muestra un mensaje
//											// deerror
//											Reporter.reportEvent(Reporter.MIC_FAIL,"Error: Documento no encontrado con tiempos ajustados.");
//											SettingsRun.exitTestIteration();
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}

		if (!isValid(documentoTx)) 
		documentoTx = obtenerNumeroTxDocumentoGeneral("Consultas");
//		documentoTx = ObtenerNumerodeTxDocumento2();
		
		WebElement compararElementos = null;
		contador = 0;
		do {
			DXCUtil.wait(1);
			contador++;
			if (!isValid(documentoTx)) {
				Reporter.reportEvent(Reporter.MIC_FAIL,"El campo Número Aprobación O Documento de la Transacción no tiene Información");
			} else {
				filaLocator = xpathdocumentoTx.replace("INUMERO", String.valueOf(contador)).replace("DocumentoTx",documentoTx);
			}

			// Valida la tabla de registros, hasta 25 registros, si no lo encuentra, cierra
			// sesion y continua al siguiente Row
			if (contador >= 25) {
				this.getDriver().switchTo().defaultContent();
				Evidence.saveAllScreens("-----------RESGISTRO NO ENCONTRADO-------------", this);
				
//				return "Registro no encontrado con numero de aprobacion:  " + documentoTx;
				
				Reporter.reportEvent(Reporter.MIC_FAIL,"Registro no encontrado con numero de aprobacion:  " + documentoTx);
				this.click(CerrSesion);
				SettingsRun.exitTestIteration();
			}

			// Si encuenta el regisro Guarda los datos de la fila en un array
			if (isValid(filaLocator))
				compararElementos = this.element(filaLocator);

			if (compararElementos != null) {
				noDocum = contador;
				for (int j = 1; j < 9; j++) {
					filaLocatorCont = xpathfilaLocatorCont.replace("I", String.valueOf(contador)).replace("J",String.valueOf(j));
					filaArray[j - 1] = this.element(filaLocatorCont).getText();
				}
				
				SettingsRun.getTestData().setParameter("Estado",filaArray[7] = this.element(filaLocatorCont).getText());
			}

		} while (compararElementos == null);
		
		if (isValid(documentoTx))
			SettingsRun.getTestData().setParameter("Número Aprobación", documentoTx);

		String tipoTx = SettingsRun.getTestData().getParameter("Servicio");

		if (tipoTx.equals("Consulta Tx Internacionales Validar Estado")) {

			this.Comprobante();

		} else {

			this.ComparacionData(filaArray);
			this.informes = new PageInformesTransInternacionales(this.pageLogin);
			this.informes.dataInformes(filaArray);
		}

	}

// ============================================[InicioConsulta]===========================================================================
	/**
	 * Metodo inicial, valida los campos y que la tabla de registros exista
	 */
	public void InicioConsulta(String servicio) throws Exception {
		
		String msg = null;

		this.switchToFrameDivisas();

		this.ErrorSesionExpirada();

		msg = this.seleccionarTransferencia("Consulta");// Se en carga de selecionar el modulo de Divisas

		if (isValid(msg))
			Reporter.reportEvent(Reporter.MIC_INFO, msg);// Retornar

		msg = this.closeActiveIntAlert();

		if (isValid(msg)) {
			Reporter.reportEvent(Reporter.MIC_INFO, msg);
		}

		Evidence.saveAllScreens("Consultas", this);

		WebElement btnBuscar = this.element(btnBuscarxpath);


		String tipoConstaTxRealizadas = "Día actual";

		if (servicio.equals("Consulta Tx Internacionales Validar Estado")) {
			tipoConstaTxRealizadas = SettingsRun.getTestData().getParameter("Tiempo de Consulta");
		}

		String ordenanteBeneficiario = SettingsRun.getTestData().getParameter("Ordenante / Nombre del beneficiario en el exterior");
		String tipoTranferencia = SettingsRun.getTestData().getParameter("Tipo de Transferencia");
		String estado = SettingsRun.getTestData().getParameter("Estado");
		String tipoMoneda = SettingsRun.getTestData().getParameter("Tipo Moneda");

		// Si no se Obtiene el dato [Número Aprobación /o Documento de la Tx] de la
		// Transacion en Linea lo obtiene desde el archivo excel si es para consulta de
		// la Tx
		
		String documentoTx = numAprova;

		if (!isValid(documentoTx)) {

			documentoTx = SettingsRun.getTestData().getParameter("Número Aprobación");

			if (isValid(documentoTx))
				numAprova = documentoTx;
		}

		// Día actual
		if (tipoConstaTxRealizadas.equals("Día actual")) {

			do {

				DXCUtil.wait(1);

			} while (this.element(btnBuscarxpath) == null);

			Reporter.write(" ");
			Reporter.write("==========[PANTALLA INICIAL DE CONSULTAS]=======================================================================================================");
			Reporter.write(" ");

			By arrayCampos[] = { title, subtitleConsulta, subtitleParametros, campoDocumento, campoTipoTrans,
					campoOrdeBene, campoEstado, campoMoneda };

			String stringArrayCampos[] = { "title", "subtitleConsulta", "subtitleParametros", "campoDocumento",
					"campoTipoTrans", "campoOrdeBene", "campoEstado", "campoMoneda" };

			int i = 0;

			do {

				DXCUtil.wait(1);
				if (this.element(arrayCampos[i]) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el campo -" + stringArrayCampos[i]);
				} else {
					Reporter.reportEvent(Reporter.MIC_PASS, "Se encuentra el campo -" + stringArrayCampos[i]);
				}

				i++;

			} while (i < 8);

			if (this.element(btnDescargar) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el boton descargar");
			} else {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se encuentra el boton descargar");
			}

			if (isElementInteractable(cmpimputOrdenanteBeneficiario))

				if (isValid(ordenanteBeneficiario))
					this.write(cmpimputOrdenanteBeneficiario, ordenanteBeneficiario);

			if (isElementInteractable(cmpimputTipoTransferencia))

				if (isValid(tipoTranferencia)) {
					msg = this.selectListItem(cmpimputTipoTransferencia, tipoTranferencia);
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);

				}

			if (isElementInteractable(cmpimputEstado))
				if (isValid(estado)) {
					msg = this.selectListItem(cmpimputEstado, estado);
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

			if (isElementInteractable(cmpimputMoneda))
				if (isValid(tipoMoneda)) {
					msg = this.selectListItem(cmpimputMoneda, tipoMoneda);
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

			this.click(btnBuscar);

		} else {

			do {
				DXCUtil.wait(1);
			} while (this.element(btnBuscarxpath) == null);

			Reporter.write(" ");
			Reporter.write("==========[PANTALLA INICIAL DE CONSULTAS]=======================================================================================================");
			Reporter.write(" ");

			By arrayCampos[] = { title, subtitleConsulta, subtitleParametros, campoDocumento, campoTipoTrans,
					campoOrdeBene, campoEstado, campoMoneda };

			String stringArrayCampos[] = { "title", "subtitleConsulta", "subtitleParametros", "campoDocumento",
					"campoTipoTrans", "campoOrdeBene", "campoEstado", "campoMoneda" };

			int i = 0;

			do {

				DXCUtil.wait(1);
				if (this.element(arrayCampos[i]) == null) {
					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el campo -" + stringArrayCampos[i]);
				} else {
					Reporter.reportEvent(Reporter.MIC_PASS, "Se encuentra el campo -" + stringArrayCampos[i]);
				}

				i++;
			} while (i < 8);

			if (this.element(btnDescargar) == null) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra el boton descargar");
			} else {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se encuentra el boton descargar");
			}

			// Rango de fechas [48 horas (2 Días) - 7 Días - 30 Días - más criterios (30 Días) - Rango de Fechas (01/02/2025 - 28/02/2025)]
			Date fechaActual = new Date();

			if (tipoConstaTxRealizadas.equals("48 horas")) {
				Date fechaDesde = DXCUtil.dateAdd(fechaActual, Calendar.DAY_OF_MONTH, -2);
				establecerRangoFechas(fechaDesde, fechaActual);

			} else if (tipoConstaTxRealizadas.equals("7 días")) {
				
				Date fechaDesde = DXCUtil.dateAdd(fechaActual, Calendar.DAY_OF_MONTH, -7);
				establecerRangoFechas(fechaDesde, fechaActual);

			} else if (tipoConstaTxRealizadas.equals("30 días") || tipoConstaTxRealizadas.equals("más criterios")) {
				
				Date fechaDesde = DXCUtil.dateAdd(fechaActual, Calendar.DAY_OF_MONTH, -30);
				establecerRangoFechas(fechaDesde, fechaActual);

			} else if (tipoConstaTxRealizadas.equals("Rango de Fechas")) {
				
//				String fechaDesdeStr = SettingsRun.getTestData().getParameter("Fecha Día Inicial  Desde (dd/mm/YYYY)").trim();
//				String fechaHastaStr = SettingsRun.getTestData().getParameter("Fecha DÍa Final Hasta (dd/mm/YYYY)").trim();
				
				String fechaDesdeStr = SettingsRun.getTestData().getSpecialParameter("Fecha Día Inicial  Desde (dd/mm/YYYY)", "Fecha");
				
				String fechaHastaStr = SettingsRun.getTestData().getSpecialParameter("Fecha DÍa Final Hasta (dd/mm/YYYY)", "Fecha");
				
				
				Date fechaDate = null;
				
				fechaDate = DXCUtil.stringToDate(fechaDesdeStr, "YYYY-MM-DD");
				
				fechaDesdeStr = DXCUtil.dateToString(fechaDate, "DD/MM/YYYY"); 
				
				
				fechaDate = DXCUtil.stringToDate(fechaHastaStr, "YYYY-MM-DD");
				
				fechaHastaStr = DXCUtil.dateToString(fechaDate, "DD/MM/YYYY"); 

				this.element(cmpimputTipoConsulta.replace("TIPODCON", "Histórico")).click();

				if (isElementInteractable(cmpimputFechaDesde)) {
					this.click(cmpimputFechaDesde);
					this.write(cmpimputFechaDesde, fechaDesdeStr);
					DXCUtil.BonotesTecla("ENTER");
				}

				if (isElementInteractable(cmpimputHasta)) {
					this.click(cmpimputHasta);
					this.write(cmpimputHasta, fechaHastaStr);
					DXCUtil.BonotesTecla("ENTER");
				}
			}

			if (isElementInteractable(cmpimputDocumento)) {

				if (isValid(documentoTx)) {
					this.click(cmpimputDocumento);
					this.write(cmpimputDocumento, documentoTx);
				}
			}


			if (isElementInteractable(cmpimputOrdenanteBeneficiario)) {

				if (isValid(ordenanteBeneficiario)) {
					this.click(cmpimputOrdenanteBeneficiario);
					this.write(cmpimputOrdenanteBeneficiario, ordenanteBeneficiario);
				}
			}

			if (isElementInteractable(cmpimputTipoTransferencia))
				if (isValid(tipoTranferencia)) {
					
					msg = this.selectListItem(cmpimputTipoTransferencia, tipoTranferencia);
					
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

			if (isElementInteractable(cmpimputEstado))
				
				if (isValid(estado)) {
					
					msg = this.selectListItem(cmpimputEstado, estado);
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}

			if (isElementInteractable(cmpimputMoneda))
				if (isValid(tipoMoneda)) {
					msg = this.selectListItem(cmpimputMoneda, tipoMoneda);
					if (isValid(msg))
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
				}
			
			Evidence.save("Filtros ingresados");

			this.click(btnBuscarxpath);

			DXCUtil.wait(12);

			Evidence.saveAllScreens("Parametros de búsqueda encontrados", this);
		}

	}

// ============================================[ComparacionData]===========================================================================	

	/**
	 * Orgaiza el array dependiendo si la transaccion es enviar o recibir compara
	 * los datos de la primera tabla de consultas
	 */
	public void ComparacionData(String filaArray[]) throws Exception {

		String ordenanteBeneficiario = null;
		String valor = null;

		String tipoTx = SettingsRun.getTestData().getParameter("Servicio").trim();
		String fechaTransaccion = SettingsRun.getTestData().getParameter("Fecha tx").trim();
		String documento = SettingsRun.getTestData().getParameter("Número Aprobación").trim();
		String tipoDeTransferencia = SettingsRun.getTestData().getParameter("Servicio").trim();
		String usuario = SettingsRun.getTestData().getParameter("Nombre de Usuario").trim();
		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
		String estado = SettingsRun.getTestData().getParameter("Estado").trim();

		if (tipoTx.contains("Enviar") || tipoTx.contains("Divisas")) {
			ordenanteBeneficiario = SettingsRun.getTestData().getParameter("Ordenante / Nombre del beneficiario en el exterior").trim();
			valor = SettingsRun.getTestData().getParameter("Valor a Pagar / Transferir").trim();

		} else if (tipoTx.contains("Recibir")) {

			ordenanteBeneficiario = SettingsRun.getTestData().getParameter("Ordenante / Nombre del beneficiario en el exterior").trim();
			
			int valor1 = Integer.parseInt(SettingsRun.getTestData().getParameter("Valor numeral cambiario 1").trim());

			int valor2;

			try {
				valor2 = Integer.parseInt(SettingsRun.getTestData().getParameter("Valor numeral cambiario 2").trim());
			} catch (Exception e) {
				valor2 = 0;
			}

			valor = Integer.toString(valor1 + valor2);
		}

		String[] headerFila = { "Fecha Transacción", "Documento", "Tipo de Transferencia", "Ordenante/Beneficiario",
				"Usuario", "Moneda", "Valor", "Estado" };

		String[] arrayExcel = { fechaTransaccion, documento, tipoDeTransferencia, ordenanteBeneficiario, usuario,
				moneda, valor, estado };

		/*
		 * Compara los datos obtenidos de la transaccion con la ventana consulta
		 */
		arrayExcel[3] = arrayExcel[3].toUpperCase();
		arrayExcel[6] = arrayExcel[6] + ",00";

		if (filaArray[0].contains(arrayExcel[0]) || arrayExcel[0].contains(filaArray[0])) {

			Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerFila[0]);
			Reporter.reportEvent(Reporter.MIC_PASS, filaArray[0]);
			Reporter.reportEvent(Reporter.MIC_PASS, arrayExcel[0]);

		} else {

			if (filaArray[0].contains("Enviada") || arrayExcel[0].contains("Enviar")) {

				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerFila[0]);
				Reporter.reportEvent(Reporter.MIC_PASS, filaArray[0]);
				Reporter.reportEvent(Reporter.MIC_PASS, arrayExcel[0]);

			} else {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerFila[0]);
				Reporter.reportEvent(Reporter.MIC_FAIL, filaArray[0]);
				Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcel[0]);
			}

		}

		for (int j = 1; j < arrayExcel.length - 1; j++) {

			if (j == 4) {
				j++;
			}

			if (arrayExcel[j].equals(filaArray[j]) || filaArray[j].equals(filaArray[j])) {

				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerFila[j]);
				Reporter.reportEvent(Reporter.MIC_PASS, filaArray[j]);
				Reporter.reportEvent(Reporter.MIC_PASS, arrayExcel[j]);

			} else {

				if (arrayExcel[j].contains("Enviada") || filaArray[j].contains("Enviar")) {

					Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerFila[j]);
					Reporter.reportEvent(Reporter.MIC_PASS, filaArray[j]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcel[j]);

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL, filaArray[j]);
					Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerFila[j]);
					Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcel[j]);
				}
			}
		}

		Reporter.write(" ");

		this.ComparacionComprobante(tipoTx);
	}

// ============================================[ComparacionComprobante]===========================================================================

	/**
	 * Almacena en un array los datos de la ventana "comprobante" dependiendo si la
	 * transaccion es enviar o recibir
	 */
	public void ComparacionComprobante(String tipoTx) throws Exception {

		Reporter.initialize(4);

		String documento = SettingsRun.getTestData().getParameter("Número Aprobación").trim();

		WebElement seleccionLinkDocumento = this.element(seleccionLinkDocumentoXpath.replace("noDocum", String.valueOf(noDocum)).replace("documento", documento));

		this.click(seleccionLinkDocumento);

		DXCUtil.wait(4);

		int contador = 0;

		String msg = this.getMsgAlertIfExistxPath("//p[1]");

		if (isValid(msg)) {

			Evidence.saveAllScreens(msg, this);
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.terminarIteracion();
		}

		WebElement getextoFecha = null;

		do {

			contador++;
			DXCUtil.wait(1);
			getextoFecha = this.element(cmpfechaTx);

			if (contador > 30) {

				Evidence.saveAllScreens("TimeOut no se presento La fecha de la tx", this);
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se presento La fecha de la tx");
				this.terminarIteracion();

			}

		} while (getextoFecha == null);

		String montoTxMov = this.getText(this.element(cmpValorTotalTx));

		DateFechaTx = this.getText(getextoFecha);

		Evidence.saveAllScreens("Historial de registros encontrados", this);

		this.ErrorSesionExpirada();

		Evidence.saveAllScreens("Comprobante", this);

		String headerComprobante[] = new String[20];
		String dataComprobante[] = new String[20];

		for (int i = 1; i < headerComprobante.length; i++) {

			headerComprobante[i - 1] = this.element(headerCompLocator.replace("I", String.valueOf(i))).getText();
			dataComprobante[i - 1] = this.element(dataCompLocator.replace("I", String.valueOf(i))).getText();

		}

		// Extrae los campos de la transaccion, para comprara, los almacena en un array
		String cuentaDestino;

		String Empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		String TipoTransferencia = SettingsRun.getTestData().getParameter("Servicio").trim();
		String Ordenante = SettingsRun.getTestData().getParameter("Ordenante / Nombre del beneficiario en el exterior").trim();
		String Numeral = SettingsRun.getTestData().getParameter("Numeral cambiario 1").trim();
		String referenciaExterna = SettingsRun.getTestData().getParameter("Referencia").trim();
		String NoCuentasDestino = SettingsRun.getTestData().getParameter("Referencia2 / Número Producto Destino").trim();
		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
		String tipoCambioUSD = SettingsRun.getTestData().getParameter("Tipo de cambio a USD").trim();
		String montoUSD = SettingsRun.getTestData().getParameter("Monto en USD").trim();
		String tasaCambio = SettingsRun.getTestData().getParameter("Tasa de cambio").trim();
		String valorCOP = SettingsRun.getTestData().getParameter("Valor de la operación en pesos colombianos").trim();
		String costoServicio = SettingsRun.getTestData().getParameter("Costo del servicio").trim();
		String ivaCostoServicio = SettingsRun.getTestData().getParameter("IVA sobre costo del servicio").trim();
		String valorNetoRecibir = SettingsRun.getTestData().getParameter("Valor Neto a recibir").trim();
		String estado = SettingsRun.getTestData().getParameter("Estado").trim();
		String fechaTransaccion = SettingsRun.getTestData().getParameter("Fecha tx").trim();
		String hora = SettingsRun.getTestData().getParameter("Hora tx").trim();

		// Campos adicionales para tx de enviar
		String cuentaOrigen = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
		String noCuenta = SettingsRun.getTestData().getParameter("Número producto origen").trim();
		String bancoDestino = SettingsRun.getTestData().getParameter("Número de código").trim();
		String beneficiario = SettingsRun.getTestData().getParameter("Ordenante / Nombre del beneficiario en el exterior").trim();
		String valorDescontado = SettingsRun.getTestData().getParameter("Valor total a descontar de la cuenta").trim();

		// Organiza lo datos a comparar, dependiendo de la transaccion si es enviar o
		// recibir
		if (tipoTx.contains("Enviar")) {

			cuentaDestino = SettingsRun.getTestData().getParameter("Número de cuenta, IBAN o CLABE").trim();
			String monto = SettingsRun.getTestData().getParameter("Valor a Pagar / Transferir").trim();

			String[] arrayExcelComprobante = { Empresa, TipoTransferencia, cuentaOrigen, noCuenta, bancoDestino,
					cuentaDestino, beneficiario, Numeral, moneda, monto, tipoCambioUSD, montoUSD, tasaCambio, valorCOP,
					costoServicio, ivaCostoServicio, valorDescontado, fechaTransaccion, hora, documento };

			this.ComparacionDataComprobanteDePago(dataComprobante, arrayExcelComprobante, headerComprobante);

		}

		if (tipoTx.contains("Recibir")) {

			cuentaDestino = SettingsRun.getTestData().getParameter("Referencia1 / Tipo Producto Destino").trim();
			String monto = SettingsRun.getTestData().getParameter("Monto").trim();

			String[] arrayExcelComprobante = { Empresa, TipoTransferencia, Ordenante, Numeral, referenciaExterna,
					cuentaDestino, NoCuentasDestino, moneda, monto, tipoCambioUSD, montoUSD, tasaCambio, valorCOP,
					costoServicio, ivaCostoServicio, valorNetoRecibir, fechaTransaccion, hora, documento };

			this.ComparacionDataComprobanteDePago(dataComprobante, arrayExcelComprobante, headerComprobante);

		}

	}

// ============================================[Comprobante]===========================================================================
	/**
	 * Comprobante dependiendo si la transaccion es enviar o recibir
	 */
	public void Comprobante() throws Exception {

		Reporter.initialize(4);

		String documento = SettingsRun.getTestData().getParameter("Número Aprobación").trim();

		WebElement seleccionLinkDocumento = this.element(seleccionLinkDocumentoXpath.replace("noDocum", String.valueOf(noDocum)).replace("documento", documento));

		this.click(seleccionLinkDocumento);

		DXCUtil.wait(4);

		int contador = 0;

		String msg = this.getMsgAlertIfExistxPath("//p[1]");

		if (isValid(msg)) {
			Evidence.saveAllScreens(msg, this);
			Reporter.reportEvent(Reporter.MIC_FAIL, msg);
			this.terminarIteracion();
		}

		WebElement getextoFecha = null;

		do {
			contador++;
			DXCUtil.wait(1);
			getextoFecha = this.element(cmpfechaTx);
			
			if (contador > 30) {
				Evidence.saveAllScreens("TimeOut no se presento La fecha de la tx", this);
				Reporter.reportEvent(Reporter.MIC_FAIL, "TimeOut no se presento La fecha de la tx");
				this.terminarIteracion();
			}
			
		} while (getextoFecha == null);

		String montoTxMov = this.getText(this.element(cmpValorTotalTx));

		DateFechaTx = this.getText(getextoFecha);

		Evidence.saveAllScreens("Historial de registros encontrados", this);

		this.ErrorSesionExpirada();

		// Comprobante Tx
		WebElement comprobante = null;

		comprobante = this.element(By.xpath("//*[@id='DescargarPDF']/div/button"));

		// Obtiene el formato de descarga desde los parámetros de prueba
		String formatodeDescarga = SettingsRun.getTestData().getParameter("Formato de Descarga");

		// Validar formatos que no requieren clic adicional
		if (formatodeDescarga.equalsIgnoreCase("EXCEL") || formatodeDescarga.equalsIgnoreCase("PDF")
				|| formatodeDescarga.equalsIgnoreCase("TEXTO")
				|| formatodeDescarga.equalsIgnoreCase("Archivo plano 360 posiciones")
				|| formatodeDescarga.equalsIgnoreCase("Enviar al Correo")) {

			Reporter.reportEvent(Reporter.MIC_INFO,"El formato seleccionado es '" + formatodeDescarga + "'. Solo se descarga el comprobante.");

			if (isElementInteractable(comprobante))
				comprobante.click();

			DXCUtil.wait(10);

			msg = this.closeActiveIntAlert();

			if (isValid(msg)) {
				Reporter.reportEvent(Reporter.MIC_INFO, msg);
			}

		} else {

			// Hacer clic según el formato solicitado
			if (formatodeDescarga.contains("Comprobante Tx")) {
				if (isElementInteractable(comprobante))
					comprobante.click();

				DXCUtil.wait(10);

				msg = this.closeActiveIntAlert();

				if (isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}
			}

			// Mensaje Swift
			WebElement swift = this.element(By.xpath("//button[contains(text(),'Mensaje Swift')]"));

			if (formatodeDescarga.contains("Mensaje Swift")) {
				if (isElementInteractable(swift))
					swift.click();

				DXCUtil.wait(10);

				msg = this.closeActiveIntAlert();

				if (isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}
			}

			// Nota de liquidación
			WebElement notadeliquidacion = null;

			notadeliquidacion = this.element(By.xpath("//button[contains(text(),'Nota de liquidación')]"));

			if (formatodeDescarga.contains("Nota de liquidación")) {

				if (isElementInteractable(notadeliquidacion))
					notadeliquidacion.click();

				DXCUtil.wait(10);

				msg = this.closeActiveIntAlert();

				if (isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}
			}

			// Declaración de cambio
			WebElement declaracion = null;

			declaracion = this.element(By.xpath("//button[contains(text(),'Declaración de cambio')]"));

			if (formatodeDescarga.contains("Declaración de cambio")) {
				if (isElementInteractable(declaracion))
					declaracion.click();

				DXCUtil.wait(10);
				
				msg = this.closeActiveIntAlert();

				if (isValid(msg)) {
					Reporter.reportEvent(Reporter.MIC_INFO, msg);
				}
			}
		}

		Evidence.saveAllScreens("Comprobante", this);
	}

// ============================================[ComparacionDataComprobanteDePago]===========================================================================

	/**
	 * Compara los datos del comprobante de pago vs los datos de la transaccion
	 */
	public void ComparacionDataComprobanteDePago(String[] dataComprobante, String[] arrayExcelComprobante, String[] headerComprobante) throws Exception {

		String tipoTx = SettingsRun.getTestData().getParameter("Servicio").trim();
		String cuentaOrigen = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
		String numeral2 = SettingsRun.getTestData().getParameter("Numeral cambiario 2").trim();

		Reporter.write(" ");
		Reporter.write("==========[COMPROBANTE DE PAGO - CONSULTAS]=======================================================================================================");
		Reporter.write(" ");

		try {
			arrayExcelComprobante[2] = arrayExcelComprobante[2].replace("Cuenta de ", "");
		} catch (Exception e) {
			arrayExcelComprobante[2] = arrayExcelComprobante[2].replace("Cuenta ", "");
		}

		arrayExcelComprobante[6] = arrayExcelComprobante[6].toUpperCase();

		if (tipoTx.contains("Enviar")) {

			dataComprobante[3] = dataComprobante[3].substring(dataComprobante[3].length() - 4);
			dataComprobante[5] = dataComprobante[5].substring(dataComprobante[5].length() - 4);
			dataComprobante[7] = dataComprobante[7].substring(0, 4);
			dataComprobante[8] = dataComprobante[8].substring(0, 3);
			arrayExcelComprobante[9] = arrayExcelComprobante[9] + ",00";

		} else {

			dataComprobante[6] = dataComprobante[6].substring(dataComprobante[6].length() - 4);
			dataComprobante[7] = dataComprobante[7].substring(0, 3);
			dataComprobante[4] = dataComprobante[4].substring(dataComprobante[4].length() - 4);

		}

		SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");

		if (tipoTx.contains("Enviar")) {

			if (!isValid(numeral2)) {
				if (dataComprobante[17].length() > 5) {
					if (!dataComprobante[17].contains(":")) {
						Date fecha = formatoEntrada.parse(dataComprobante[17]);
						String fechaGirada = formatoSalida.format(fecha);
						dataComprobante[17] = fechaGirada;
					}
				}

			} else {

				if (dataComprobante[18].length() > 5) {
					Date fecha = formatoEntrada.parse(dataComprobante[18]);
					String fechaGirada = formatoSalida.format(fecha);
					dataComprobante[18] = fechaGirada;

					// Guardar el valor en la posición 8 en una variable temporal
					String valorTemporal = arrayExcelComprobante[8];

					// Mover los elementos hacia adelante desde la posición 9 hasta el final
					for (int i = arrayExcelComprobante.length - 1; i > 8; i--) {
						arrayExcelComprobante[i] = arrayExcelComprobante[i - 1];
					}

					// Colocar el valor temporal en la posición 9 (anterior posición 8)
					arrayExcelComprobante[8 + 1] = valorTemporal;
				}

			}

		} else {

			if (!isValid(numeral2)) {

				if (dataComprobante[16].length() > 5) {
					Date fecha = formatoEntrada.parse(dataComprobante[16]);
					String fechaGirada = formatoSalida.format(fecha);
					dataComprobante[16] = fechaGirada;
				}

			} else {

				if (dataComprobante[16].length() > 5) {
					Date fecha = formatoEntrada.parse(dataComprobante[16]);
					String fechaGirada = formatoSalida.format(fecha);
					dataComprobante[17] = fechaGirada;

					// Guardar el valor en la posición 8 en una variable temporal
					String valorTemporal = arrayExcelComprobante[8];

					// Mover los elementos hacia adelante desde la posición 9 hasta el final
					for (int i = arrayExcelComprobante.length - 1; i > 8; i--) {
						arrayExcelComprobante[i] = arrayExcelComprobante[i - 1];
					}

					// Colocar el valor temporal en la posición 9 (anterior posición 8)
					arrayExcelComprobante[8 + 1] = valorTemporal;
				}

			}
		}

		for (int j = 0; j < dataComprobante.length - 1; j++) {

			if (j == 2 && dataComprobante[2].contains(arrayExcelComprobante[2])) {

				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[2]);
				Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[2]);
				Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[2]);
				j++;

			} else if (j == 2 && !dataComprobante[2].contains(arrayExcelComprobante[2])) {

				if (dataComprobante[2].contains(arrayExcelComprobante[2])
						|| arrayExcelComprobante[2].contains(dataComprobante[2])) {

					Reporter.reportEvent(Reporter.MIC_PASS, "Los datoscoinciden en: " + headerComprobante[2]);
					Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[2]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[2]);
					j++;

				} else {

					if (dataComprobante[2].contains(arrayExcelComprobante[2])
							|| arrayExcelComprobante[2].contains(dataComprobante[2])) {

						Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[2]);
						Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[2]);
						Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[2]);
						j++;

					} else {

						if ((arrayExcelComprobante[2].contains("Ahorros")
								&& dataComprobante[2].contains("CUENTA AHORROS"))
								|| (arrayExcelComprobante[2].contains("Ahorros")
										&& dataComprobante[2].contains("ahorros"))
								|| (arrayExcelComprobante[2].contains("ahorros")
										&& dataComprobante[2].contains("Ahorros"))
								|| (arrayExcelComprobante[2].contains("corriente")
										&& dataComprobante[2].contains("Corriente"))) {

							Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[2]);
							Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[2]);
							Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[2]);
							j++;

						} else {

							Reporter.reportEvent(Reporter.MIC_FAIL,
									"Los datos NO coinciden en: " + headerComprobante[2]);
							Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[2]);
							Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[2]);
							j++;

						}
					}

				}

			}

			if (tipoTx.contains("Enviar")) {

				if (j == 3 && arrayExcelComprobante[3].contains(dataComprobante[3])) {
					Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[3]);
					j++;

				} else if (j == 3 && !dataComprobante[2].contains(arrayExcelComprobante[3])) {

					Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[3]);
					j++;

				}

				if (j == 4 && dataComprobante[4].contains(arrayExcelComprobante[4])) {

					Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[4]);
					Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[4]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[4]);
					j++;

				} else if (j == 4 && !dataComprobante[4].contains(arrayExcelComprobante[4])) {

					if (dataComprobante[4].contains(arrayExcelComprobante[4])
							|| arrayExcelComprobante[4].contains(dataComprobante[4])) {

						Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[4]);
						Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[4]);
						Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[4]);
						j++;

					} else {

						if (dataComprobante[4].contains(arrayExcelComprobante[4])
								|| arrayExcelComprobante[4].contains(dataComprobante[4])) {

							Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[4]);
							Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[4]);
							Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[4]);
							j++;

						} else {

							Reporter.reportEvent(Reporter.MIC_FAIL,"Los datos NO coinciden en: " + headerComprobante[4]);
							Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[4]);
							Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[4]);
							j++;

						}

					}
				}

			} else {

				if (j == 3 && dataComprobante[3].contains(arrayExcelComprobante[3])) {

					Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[3]);
					j++;

				} else if (j == 3 && !dataComprobante[2].contains(arrayExcelComprobante[3])) {

					Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[3]);
					Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[3]);
					j++;

				}

				if (j == 4 && arrayExcelComprobante[4].contains(dataComprobante[4])) {

					Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[4]);
					Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[4]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[4]);
					j++;

				} else if (j == 4 && !arrayExcelComprobante[4].contains(dataComprobante[4])) {

					Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerComprobante[4]);
					Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[4]);
					Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[4]);
					j++;

				}

				if (j == 5) {
					j = j + 2;
				}
			}

			if (j == 5 && arrayExcelComprobante[5].contains(dataComprobante[5])) {

				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[5]);
				Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[5]);
				Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[5]);
				j++;

			} else if (j == 5 && !dataComprobante[2].contains(arrayExcelComprobante[5])) {

				Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerComprobante[5]);
				Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[5]);
				Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[5]);
				j++;

			}

			if (!numeral2.equals("") && tipoTx.contains("Enviar") && j == 8) {
				j++;
			}

			if (dataComprobante[j].equals(arrayExcelComprobante[j])|| arrayExcelComprobante[j].equals(dataComprobante[j])) {

				Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[j]);
				Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[j]);
				Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[j]);
				j++;

			} else {

				if (dataComprobante[j].contains("Enviada") || arrayExcelComprobante[j].contains("Enviar")) {
					Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[j]);
					Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[j]);
					Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[j]);
					j++;

				} else {

					if (arrayExcelComprobante[j].contains(dataComprobante[j]) || dataComprobante[j].contains(arrayExcelComprobante[j])) {

						Reporter.reportEvent(Reporter.MIC_PASS, "Los datos coinciden en: " + headerComprobante[j]);
						Reporter.reportEvent(Reporter.MIC_PASS, dataComprobante[j]);
						Reporter.reportEvent(Reporter.MIC_PASS, arrayExcelComprobante[j]);
						j++;
					}else {
						//Arreglar Datos Juan
						Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos NO coinciden en: " + headerComprobante[j]);
						Reporter.reportEvent(Reporter.MIC_FAIL, dataComprobante[j]);
						Reporter.reportEvent(Reporter.MIC_FAIL, arrayExcelComprobante[j]);
						j++;
					}
				}
			}

			DXCUtil.wait(1);

		}

		Reporter.write(	"=================================================================================================================");

		this.informes = new PageInformesTransInternacionales(this.pageLogin);
		this.informes.dataComprobanteInformes(dataComprobante);

	}

// ============================================[ValidarCCIU]===========================================================================

	public void ValidarCCIU() throws Exception {

		String numeroIDEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		String reportMsg = null;

		DXCUtil.BonotesTecla("ALTTAB");

		this.pageInicioC360.irAModulo(PageInicioC360.MOD_PAGINA_INICIAL);
		
		reportMsg = this.pageEmpresasC360.buscarEmpresaC360(numeroIDEmpresa);
		
		if (!reportMsg.isEmpty() && (!reportMsg.contains("El cliente no ha actualizado")))
			this.pageInicioC360.terminarIteracion(Reporter.MIC_NOEXEC, "[ERROR DATA] " + reportMsg);

		Reporter.reportEvent(Reporter.MIC_PASS,"Se ha encontrado la empresa con el número de identificación [" + numeroIDEmpresa + "]");
		
//		String masIfEmpres[] = this.pageEmpresasC360.getMasInfoClienteEmpresa();
//		
//		for (String masIfEmprepos :  masIfEmpres) {
//			System.out.println(masIfEmprepos);
//		}

		DXCUtil.BonotesTecla("ALTTAB");

		this.pageInicioC360.cerrarSesion();
	}

// ============================================[SelectCalendarDate]===========================================================================

	/**
	 * Metodo para seleccionar una fecha del calendario
	 * 
	 * @param fecha            Se toma del archivo de excel en formato DD/MM/AAAA
	 * @param abrirCalendario  Localizador para seleccionar calendario
	 * @param mes              Localizador para seleccionar el mes
	 * @param año              Localizador para seleccionar el año
	 * @param elementoDiaXpath localizador xPath que debe tener el texto 'dia' para
	 *                         ser remplazado en el metodo por el dia ingresado en
	 *                         el campo fecha
	 */
	public void SelectCalendarDate(String fecha, By abrirCalendario, By mes, String elementoDiaXpath, By año) {
		// Divide la fecha en día, mes y año
		String[] dateParts = fecha.split("/");
		String day = dateParts[0];
		String month = dateParts[1];
		String year = dateParts[2];

		// Convertir el día a un número entero para eliminar los ceros a la izquierda

		int dayInt = Integer.parseInt(day);
		String dayFormatted = String.valueOf(dayInt);

		String dayElementRemplazado = elementoDiaXpath.replace("dia", dayFormatted);
		// Convertir el mes a un número entero para eliminar los ceros a la izquierda
		int monthInt = Integer.parseInt(month);
		String monthFormatted = String.valueOf(monthInt);

		// Mapear el mes numérico a su representación en texto
		String[] months = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
		String monthText = months[Integer.parseInt(monthFormatted) - 1];

		// Abre el calendario
		WebElement calendarButton = this.findElement(abrirCalendario);
		calendarButton.click();

		// Espera a que el calendario sea visible
		int cont = 0;
		do {
			DXCUtil.wait(1);
			cont++;
		} while (mes == null && cont < 5);
		if (mes == null) {
			DXCUtil.wait(2);
			calendarButton.click();
		}

		// Selecciona el mes
		Select monthSelect = new Select(this.findElement(mes));
		monthSelect.selectByVisibleText(monthText);

		// Selecciona el año
		Select yearSelect = new Select(this.findElement(año));
		yearSelect.selectByVisibleText(year);

		// Selecciona el día
		WebElement dayElementoFinal = null;
		dayElementoFinal = this.element(dayElementRemplazado);
		dayElementoFinal.click();
	}

// ============================================[establecerRangoFechas]===========================================================================

	/**
	 * Método auxiliar para establecer rango de fechas
	 * 
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */
	private void establecerRangoFechas(Date fechaDesde, Date fechaHasta) throws Exception {
		String strFechaDesde = DXCUtil.dateToString(fechaDesde, "dd/MM/yyyy");
		String strFechaHasta = DXCUtil.dateToString(fechaHasta, "dd/MM/yyyy");

		this.element(cmpimputTipoConsulta.replace("TIPODCON", "Histórico")).click();

		if (isElementInteractable(cmpimputFechaDesde)) {
			this.click(cmpimputFechaDesde);
			this.write(cmpimputFechaDesde, strFechaDesde);
			DXCUtil.BonotesTecla("ENTER");
		}

		if (isElementInteractable(cmpimputHasta)) {
			this.click(cmpimputHasta);
			this.write(cmpimputHasta, strFechaHasta);
			DXCUtil.BonotesTecla("ENTER");
		}
	}

// ============================================[MsjSinRegistros]===========================================================================

	/**
	 * Omite el mensaje por si no hay ningun registro disponible
	 */
	public void MsjSinRegistros() throws Exception {

		int time = 0;

		do {
			DXCUtil.wait(1);
			time++;
			if (this.element(aler) != null) {
				Evidence.saveAllScreens("No existen resultados para esta búsqueda", this);
				this.getDriver().switchTo().defaultContent();
				this.click(CerrSesion);
				SettingsRun.exitTestIteration();
			}

		} while (time < 4);

	}

// ============================================[findDocumentWithTimeAfterDelay]===========================================================================

//	private String findDocumentWithTimeAfterDelay(String fecha, String horaconvert) throws Exception {
//		// Convierte minutos a milisegundos
//		return findDocumentWithTime(fecha, horaconvert);
//	}

// ============================================[findDocumentWithTime]===========================================================================	

//	/**
//	 * Este metodo Obtiene el [Número de tx o Documento de la Tx]
//	 * 
//	 * @param fecha       Tx
//	 * @param horaconvert Tx
//	 * @return Retorna el [Número de tx o Documento de la Tx]
//	 * @throws Exception
//	 */
//	private String findDocumentWithTime(String fecha, String horaconvert) throws Exception {
//
//		String moneda = SettingsRun.getTestData().getParameter("Tipo Moneda").trim();
//
//		try {
//
//			String fechayHora = fecha + " " + horaconvert;
//			String servicio = SettingsRun.getTestData().getParameter("Servicio");
//			String fechayHoraconver = DXCUtil.convertirFecha_Y_HoraSiPM(fechayHora);
//
//			if (servicio.equals("Consulta Tx Internacionales Enviar al exterior Validar Estado")) {
//				fechayHoraconver = fechayHora;
//			}
//
//			String obTNumDocumTxCon = this.element(xpathNumDocumTxCon.replace("fechayhoraconvert", fechayHoraconver).replace("MONEDA", moneda)).getText();
//
//			return obTNumDocumTxCon;
//
//		} catch (Exception e) {
//
//			return null;
//		}
//
//	}

// ============================================[ValidacionesStratusConsulta]===========================================================================

	public void ValidacionesStratusConsulta() throws Exception {

		int contador = 0;

		if (!DatosDavivienda.IS_RISKMOTOR) {

			// fechaInicial - Date que contiene la fecha de consulta inicial.

			Date fechaConsulta = DXCUtil.stringToDate(DateFechaTx, "YYYY/MM/DD");
			Date FechaConsultaStratus = DXCUtil.dateAdd(fechaConsulta, Calendar.MINUTE, -1);

			// Datos del titular como un array de String Stratus

			String tipoDocumento = SettingsRun.getTestData().getParameter("Tipo Identificación");
			String numeroDoc = SettingsRun.getTestData().getParameter("Id usuario").trim();
			String tipoProduct = SettingsRun.getTestData().getParameter("Tipo producto origen / Franquicia").trim();
			String numeroProducto = SettingsRun.getTestData().getParameter("Número producto origen").trim();

			// Datos usuario
			String[] datosTitular = { tipoDocumento, numeroDoc };
			// Obtiene el tipo de cuenta si es migrada o fondo o tarjeta de cradito
			String tipoProd = Stratus.getTipoCuenta(tipoProduct);
			// Ventanas en consulta de movimientos Stratus
			String[] arrayVents = { MovimientoStratus.VENT_UNID, MovimientoStratus.VENT_IVA,
					MovimientoStratus.VENT_SALDO, MovimientoStratus.VENT_EMERG };

			// Validacion con Stratus
			if (DatosDavivienda.STRATUS != null) {
				Reporter.write(" ");
				Reporter.write("*** CONSULTA DE MOVIMIENTOS EN STRATUS");
				Reporter.write(" ");
				Reporter.reportEvent(Reporter.MIC_INFO, "Fecha y hora de la tx: " + FechaConsultaStratus);
				Reporter.write(" ");
				Reporter.write(
						"           FECHA | HORA |   TALON  | TIPO  |  OFRE | VALOR TOTAL | MTVO  |  UNIDAD  |  VALOR CHEQUE  |  VALOR IVA  |  SALDO ANTERIOR  |  GMF | IND");
				Reporter.write(" ");
				// Realiza la consulta en Stratus de todos los movimientos

				List<MovimientoStratus> datosCtadh2 = DatosDavivienda.STRATUS.getMovimientosEnRangoStratus(tipoProd,
						numeroProducto, arrayVents, FechaConsultaStratus, 10, datosTitular);

				List<String[]> movimientosStratus = new ArrayList<>();

				String saldoinicialstra = this.saldoTotalInicial;
				String saldoDislstra = this.saldoDisInicial;

				String saldofinal = this.saldoTotalFinal;
				String saldoDisponiblefinal = this.saldoDisponibleFinal;

				// Saldo inicial
				double saldoTotalInicial1 = 0.0;
				double saldoDisponible = 0.0;
				saldoTotalInicial1 = Double.parseDouble(saldoDislstra);
				saldoDisponible = Double.parseDouble(saldoinicialstra);
				String saldoPos = String.format("%.1f", saldoDisponible);

				// Saldo final
				double saldoTotalFinal = 0.0;
				double saldoDisponibleFinal = 0.0;
				saldoTotalFinal = Double.parseDouble(saldoDisponiblefinal);
				saldoDisponibleFinal = Double.parseDouble(saldoDisponiblefinal);
				String saldoPosFinal = String.format("%.2f", saldoTotalFinal);

				// Retorna los movimientos en Stratus
				for (MovimientoStratus movimiento : datosCtadh2) {
					DXCUtil.waitMilisegundos(125);
					String oficinaStratus = null, tipoStratus = null, mtvoStratus = null, talon = null;
					double ValorTotalMov = 0.0, ValorChequeMovStartus = 0.0;
					double saldoAnteriorStratus = 0.0;
					double unidad = 0.0;

					String movToString = movimiento.toString().replace("[INDIC:", "").replace("]", "")
							.replace("SALDO:", "|").replace("UNID :", "|").replace("EMERG:", "|").replace("IVA  :", "|")
							.replace(" ", "").replace("  ", "");

					String[] datos = movToString.split("\\|");// Extraer los datos necesarios por posiciones

					String fecha = datos[0].trim();
					String hora = datos[1].trim();
					String term = datos[2].trim();
					talon = datos[3].trim();
					tipoStratus = datos[4].trim();
					oficinaStratus = datos[5].trim();
					String valorStratus = datos[6].trim();
					ValorTotalMov = Double.parseDouble(valorStratus);
					mtvoStratus = datos[7].trim();
					String FechaMovStratus = datos[8].trim();
					String saldoAnterior = datos[9].trim();
					saldoAnteriorStratus = Double.parseDouble(saldoAnterior);
					String saldoUnidad = datos[10].trim();
					unidad = Double.parseDouble(saldoUnidad);
					String chequeStratus = datos[11];
					String in = datos[12];
					String valorEmerg = datos[13];
					String indCancelado = datos[14];
					String ivam = datos[15];

					String msgcancelado = " ";

					if (indCancelado != null && indCancelado.equals("true")) {
						msgcancelado = "Cancelado";
					}

					Reporter.write("Movimiento: " + fecha + " | " + hora + " | " + talon + " | " + tipoStratus + "  |  "
							+ oficinaStratus + " | " + BigDecimal.valueOf(ValorTotalMov) + " | " + mtvoStratus + "  |  "
							+ BigDecimal.valueOf(unidad) + "  |  " + BigDecimal.valueOf(ValorChequeMovStartus) + "  |  "
							+ ivam + "  |  " + BigDecimal.valueOf(saldoAnteriorStratus) + "  |  " + valorEmerg + " | "
							+ msgcancelado);

					String movr = fecha + " " + hora + " " + talon + " " + tipoStratus + " " + oficinaStratus + " "
							+ BigDecimal.valueOf(ValorTotalMov) + " " + mtvoStratus + " " + BigDecimal.valueOf(unidad)
							+ " " + BigDecimal.valueOf(ValorChequeMovStartus) + " " + ivam + " " + saldoAnterior + " "
							+ valorEmerg + " " + indCancelado;

					if (movr.contains("0034") || movr.contains("0055")) {
						movimientosStratus.add(movr.split(","));
					}

				}

				double sumaMovimientos = 0.0;
				double sumaunida = 0.0;
				double sumaCheque = 0.0;
				double sumaIva = 0.0;
				double sumaGmf = 0.0;
				double saldoTotalFinalEsperado = 0.0;

				int posicionSaldoInicialpos = -1;

				String servicio = SettingsRun.getTestData().getParameter("Servicio").trim();

				for (int i = 0; i < movimientosStratus.size(); i++) {
					String[] movimiento = movimientosStratus.get(i);
					if (movimiento[movimiento.length - 1].contains(saldoPos.substring(0, saldoPos.length() - 3))) {
						posicionSaldoInicialpos = i;
						break;
					}
				}

				Reporter.write(" ");
				Reporter.write("*** Valores Cobros, IVA y GMF");
				Reporter.write(" ");

				String porGmf = DatosDavivienda.STRATUS.getGMF();

				double cobrovalorIva = 0.0;
				double cobrovalorGMF = 0.0;
				if (posicionSaldoInicialpos != -1) {

					// Sumar todos los movimientos, IVA y GMF
					for (int i = posicionSaldoInicialpos; i < movimientosStratus.size(); i++) {
						String[] movimiento = movimientosStratus.get(i);
						String[] datosMov = movimiento[0].split(" ");

						double valorMovimiento = Double.parseDouble(datosMov[5].trim().replace(",", ""));
						String tipoMovimiento = datosMov[6].trim();
						double valorunida = Double.parseDouble(datosMov[7].trim().replace(",", ""));
						double valorCheque = Double.parseDouble(datosMov[8].trim().replace(",", ""));
						double valorIva = Double.parseDouble(datosMov[9].trim().replace(",", ""));
						double emerg = Double.parseDouble(datosMov[11].trim().replace(",", ""));
						String cancelados = datosMov[12].trim();

						sumaMovimientos += valorMovimiento;
						sumaunida += valorunida;
						sumaCheque += valorCheque;
						sumaIva += Math.round(valorIva);
						sumaGmf += emerg;

						if (valorIva != 0) {

							Reporter.write("Valor Cobro Iva Movimiento: " + BigDecimal.valueOf(valorIva));
							cobrovalorIva = (valorMovimiento * 19 / 100);
							Reporter.write("Compara el Valor del Iva  : " + BigDecimal.valueOf(cobrovalorIva));

							cobrovalorIva = valorIva;

						} else if (valorIva == 0 && valorunida != 0) {
//								cobrovalorIva = (valorunida * 19 / 100);
							Reporter.write("Para los movimientos cobros o 6 No se realiza la suma del Iva");
							Reporter.write(" ");
							Reporter.write("Posible Valor Cobro Iva que podria aparecer: "
									+ BigDecimal.valueOf((valorunida * 19 / 100)));
						} else if (valorIva == 0 && valorunida == 0) {
							cobrovalorIva = (valorunida * 19 / 100);
							Reporter.write("Valor Cobro Iva: " + BigDecimal.valueOf(cobrovalorIva));
						}

						if (valorunida != 0) {
							if (valorIva == 0) {

								if (porGmf.contains("1.0040")) {
									Reporter.write("Valor Cobro  GMF 4*1000: "
											+ BigDecimal.valueOf((valorMovimiento / 1000 * 4)));
									cobrovalorGMF = (valorMovimiento / 1000 * 4);
								} else if (porGmf.contains("1.0020")) {
									Reporter.write("Valor Cobro  GMF 2*1000: "
											+ BigDecimal.valueOf((valorMovimiento / 1000 * 2)));
									cobrovalorGMF = (valorMovimiento / 1000 * 2);
								} else if (porGmf.contains("1.0030")) {
									Reporter.write("Valor Cobro  GMF 3*1000: "
											+ BigDecimal.valueOf((valorMovimiento / 1000 * 3)));
									cobrovalorGMF = (valorMovimiento / 1000 * 3);
								} else if (porGmf.contains("1.0035")) {
									Reporter.write("Valor Cobro  GMF 35*1000: "
											+ BigDecimal.valueOf((valorMovimiento / 1000 * 35)));
									cobrovalorGMF = (valorMovimiento / 1000 * 35);
								}
							} else if (valorIva != 0) {
								double cobrosuma = (valorMovimiento + cobrovalorIva);
								if (porGmf.contains("1.0040")) {

									Reporter.write(
											"Valor Cobro  GMF 4*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 4)));
									cobrovalorGMF = (cobrosuma / 1000 * 4);
								} else if (porGmf.contains("1.0020")) {
									Reporter.write(
											"Valor Cobro  GMF 2*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 2)));
									cobrovalorGMF = (cobrosuma / 1000 * 2);
								} else if (porGmf.contains("1.0030")) {
									Reporter.write(
											"Valor Cobro  GMF 3*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 3)));
									cobrovalorGMF = (cobrosuma / 1000 * 3);
								} else if (porGmf.contains("1.0035")) {
									Reporter.write(
											"Valor Cobro  GMF 35*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 35)));
									cobrovalorGMF = (cobrosuma / 1000 * 35);
								}
							}
						} else if (valorunida == 0) {
							double cobrosuma = (valorMovimiento + cobrovalorIva);
							if (porGmf.contains("1.0040")) {
								Reporter.write(
										"Valor Cobro  GMF 4*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 4)));
								cobrovalorGMF = (cobrosuma / 1000 * 4);
							} else if (porGmf.contains("1.0020")) {
								Reporter.write(
										"Valor Cobro  GMF 2*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 2)));
								cobrovalorGMF = (cobrosuma / 1000 * 2);
							} else if (porGmf.contains("1.0030")) {
								Reporter.write(
										"Valor Cobro  GMF 3*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 3)));
								cobrovalorGMF = (cobrosuma / 1000 * 3);
							} else if (porGmf.contains("1.0035")) {
								Reporter.write(
										"Valor Cobro  GMF 35*1000: " + BigDecimal.valueOf((cobrosuma / 1000 * 35)));
								cobrovalorGMF = (cobrosuma / 1000 * 35);
							}
						}

						if (valorIva != 0) {
							double cobrosuma = (valorMovimiento + cobrovalorIva);

							Reporter.write("Valor de la Tx: " + BigDecimal.valueOf(valorMovimiento)
									+ " Mas el Valor IVA: " + BigDecimal.valueOf(cobrovalorIva) + " La Suma es de: "
									+ BigDecimal.valueOf(cobrosuma));

							Reporter.write(" ");

							Reporter.write("Valor Unidad: " + (cobrosuma + cobrovalorGMF));
						}

						if (valorIva == 0) {
							double cobrosuma = (valorMovimiento + cobrovalorGMF);
							Reporter.write("Valor de la Tx: " + BigDecimal.valueOf(valorMovimiento)
									+ " Mas el Valor Gmf: " + BigDecimal.valueOf(cobrovalorGMF) + " La Suma es de: "
									+ BigDecimal.valueOf(cobrosuma));

							Reporter.write(" ");
							Reporter.write("Valor Unidad: " + cobrosuma);
						}

						Reporter.write(" ");

					}

					double restaMovimientos = 0.0;
					double restaunida = 0.0;
					double restaCheque = 0.0;
					double restaIva = 0.0;
					double restaGmf = 0.0;
					String cancelados = "false";
					int canNumCancelados = 0;
					// Restar movimientos cancelados y tipoMovimiento 307
					for (int i = posicionSaldoInicialpos; i < movimientosStratus.size(); i++) {
						String[] movimiento = movimientosStratus.get(i);
						String[] datosMov = movimiento[0].split(" ");

						double valorMovimiento = Double.parseDouble(datosMov[5].trim().replace(",", ""));
						String tipoMovimiento = datosMov[6].trim();
						double valorunida = Double.parseDouble(datosMov[7].trim().replace(",", ""));
						double valorCheque = Double.parseDouble(datosMov[8].trim().replace(",", ""));
						double valorIva = Double.parseDouble(datosMov[9].trim().replace(",", ""));
						double emerg = Double.parseDouble(datosMov[11].trim().replace(",", ""));
						cancelados = datosMov[12].trim();

						if (tipoMovimiento.equals("307") || tipoMovimiento.equals("34")
								|| tipoMovimiento.equals("741")) {
							restaMovimientos += valorMovimiento;
							restaunida += valorunida;
							restaCheque += valorCheque;
							restaIva += valorIva;
							restaGmf += emerg;
							canNumCancelados++;
						} else if (tipoMovimiento.equals("307") || tipoMovimiento.equals("34")
								|| tipoMovimiento.equals("741") || cancelados.equals("true")) {
							restaMovimientos += valorMovimiento;
							restaunida += valorunida;
							restaCheque += valorCheque;
							restaIva += Math.round(valorIva);
							restaGmf += emerg;
							canNumCancelados++;
						}
					}

					double valorMov = 0.0;
					valorMov = (sumaMovimientos - restaMovimientos);

					double valorUnidad = 0.0;
					valorUnidad = (sumaunida - restaunida);

					double valorCheque = 0.0;
					valorCheque = (sumaCheque - restaCheque);

					double valorIva = 0.0;
					valorIva = (sumaIva - restaIva);

					double valorGmf = 0.0;
					valorGmf = (sumaGmf - restaGmf);

					Reporter.write(" ");
					Reporter.write("*** VALIDACIÓN DEL MOVIMIENTO");
					Reporter.write(" ");

					Reporter.write("SUMA de los Movimientos: " + BigDecimal.valueOf(sumaMovimientos));
					Reporter.write("SUMA de las Unidades: " + BigDecimal.valueOf(sumaunida));
					Reporter.write("SUMA de los Cheques: " + BigDecimal.valueOf(sumaCheque));
					Reporter.write("SUMA de los Iva: " + BigDecimal.valueOf(sumaIva));
					Reporter.write("SUMA de los GMF: " + BigDecimal.valueOf(sumaGmf));
					Reporter.write(" ");

					Reporter.write("Total de Movimientos Cancelados: " + canNumCancelados);

					if (cancelados.equals("true")) {

						Reporter.write("SUMA de los Movimiento cancelado: " + BigDecimal.valueOf(restaMovimientos));
						Reporter.write("SUMA de los Unidad cancelado: " + BigDecimal.valueOf(restaunida));
						Reporter.write("SUMA de los Cheque cancelado: " + BigDecimal.valueOf(restaCheque));
						Reporter.write("SUMA de los Iva cancelado: " + BigDecimal.valueOf(restaIva));
						Reporter.write("SUMA de los GMF cancelado: " + BigDecimal.valueOf(restaGmf));
						Reporter.write(" ");
					}

					double costoTx = 0.0;
					costoTx = (sumaMovimientos + restaMovimientos);

					// Restar los montos al saldo inicial
					double saldoesperado = 0.0;

					if (valorUnidad != 0) {
						saldoesperado = (saldoTotalInicial1 - valorUnidad - valorCheque);
					} else if (valorUnidad == 0) {
						saldoesperado = (saldoTotalInicial1 - valorMov - Math.round(valorIva) - valorGmf);

					}

					Reporter.write(" ");
					Reporter.write("*** HACIENDO LAS VALIDACIÓN DEL MOVIMIENTO");
					Reporter.write(" ");
					Reporter.write("Valor del Movimiento: " + BigDecimal.valueOf(valorMov));
					Reporter.write("Valor del Unidad: " + BigDecimal.valueOf(valorUnidad));
					Reporter.write("Valor del Cheque " + BigDecimal.valueOf(valorCheque));

					Reporter.write("Valor del Iva: " + BigDecimal.valueOf(valorIva));
					Reporter.write("Valor del GMF " + BigDecimal.valueOf(valorGmf));

					saldoTotalFinalEsperado = saldoesperado;

					// Calcular la diferencia entre los saldos finales
					double diferencia = 0.0;

					diferencia = (saldoTotalFinal - saldoTotalFinalEsperado);
					String saldoEsperado = String.format("%.1f", saldoTotalFinalEsperado);
					Reporter.write(" ");
					// Mostrar los resultados
					Reporter.write(" ");
					Reporter.reportEvent(Reporter.MIC_HEADER, "INFRORMACIÓN DE SALDO FINAL");
					Reporter.write(" ");
					Reporter.reportEvent(Reporter.MIC_INFO, "El Saldo Final Stratus : " + saldoDisponiblefinal);

					Reporter.reportEvent(Reporter.MIC_INFO,
							"El Saldo Final esperado: " + BigDecimal.valueOf(saldoTotalFinalEsperado));

					Reporter.write(" ");
					Reporter.reportEvent(Reporter.MIC_HEADER, "DIFERENCIAL ENTRE LOS SALDOS");
					Reporter.write(" ");

					Reporter.reportEvent(Reporter.MIC_INFO,
							"La diferencia entre los saldos final es: " + BigDecimal.valueOf(Math.round(diferencia)));

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontró el saldo inicial en los movimientos.");

				}
			}

		}
	}

}