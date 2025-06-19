package launchTest;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;

import dav.Consultas_Y_Extractos.*;
import dav.middlePymes.ControllerValiPymeMiddle;
import dav.middlePymes.PageUsuariosEmpresa;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloTx.PageOrigen;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MovimientoStratus;
import dav.transversal.Stratus;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class Launch_ConsultasYEstractos extends BaseTestNG {

	// =======================================================================================================================
	// Page & Crontroller
	PageLoginPymes pageLogin = null;
	PageOrigen pageOrigen = null;
	PageUsuariosEmpresa pageUsuariosEmpresa = null;
	PageConsultasyExtractos pageConsultasyExtractos = null;
	PageSaldos pageSaldos = null;
	PageCheques pageCheques = null;
	PageTxaTravezDelPortal pageTxaTravezDelPortal = null;
	ControllerValiPymeMiddle controllerValiPymeMiddle = null;

	Properties info;
	String stratus = null;
	// =======================================================================================================================
	String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre;

	// =======================================================================================================================
	// VARIABLES GLOBALES DE TX Y MOTOR

	final String HA_CONS_DESDE_LISTA = "SI";
	final String CN_APRO_PEND = "1";

// -----------------------------------------------------------------------------------------------------------------------

	String contratacion = null;
	String servicio, navegador,producto,empresa,tiempoconsulta, informe;
	String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
	String na = "N/A";
	// =======================================================================================================================

	// =======================================================================================================================
	// LOCATOR PYME FRONT
	By log30 = By.xpath("/html/body/h2[1]/p");
	By locCmEmpresa = By.xpath("//select[@id='dropMasterEmpresa']");

	// Login Front
	By cerrarSesion = By.xpath("//*[@id='CerrarSesion']");
	By cmCerrSes = By.cssSelector("a[id='CerrarSesion']");
	By fecha = By.xpath("//*[@id='tabla-top']/tbody/tr[1]/td[1]");

	// =======================================================================================================================

	// ================================LAUNCHDATA=======================================================================================

	public void launchData() { // DATOS DEL LANZAMIENTO
//		Reporter.initializeEvidenceType(new EvidencePdfFile());// INDICAR LA TOMA DE EVIDENCIAS Y EL FORMATO
//		Evidence.siTomarla(Evidence.FORMATO_WORD); // TOMAR EVIDENCIAS POR ROW Y DEJA LO ÚLTIMO EN EL FORMATO INDICADO
		Reporter.initializeEvidenceType(new EvidencePdfFile());
		SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER ESTÁ EN LA FILA 4 : USARLO SI EL HEADER NO ESTÁ
										// EN 1
		Reporter.writeTitle("\n*** PRUEBAS PORTAL PYME ***");

		// 1.1: Abrir WinAppDriver si no se encuentra abierto - isaac
		DXCUtil.startWinAppDriver();
		// -----------------------------------------------------------------------------------------------------------------------

		// PARÁMETROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE ESTÉ
		// HACIENDO
		SettingsRun.ARRAY_DATA_PARAMS = new String[] { "Selección" };

		// -----------------------------------------------------------------------------------------------------------------------
	}
	
//	public void cargarTitleYDatosEvidence() throws Exception { // PARA TÍTULOS Y DATA DE EVIDENCIAS
//		super.cargarTitleYDatosEvidence();
//		// CUANDO SE TRATA DE EXCEL O WORD PARA LA CARGA DE EVIDENCIAS, ESTOS SON LOS DATOS CLAVE A MOSTRAR
//		Evidence.testData = "Sistema Operativo" + " - " + "Dato1" + " - " + "Dato2";
//	}
	// =======================================================================================================================

	// =======================================================================================================================

	// ====================================LAUNCHCLOSE===================================================================================

	public void launchClose() { // CIERRE DEL LANZAMIENTO

	
		
		if (this.pageLogin != null)
		if (this.pageLogin.ThereareOpenWindows())
			this.pageLogin.closeAllBrowsers();
		// -----------------------------------------------------------------------------------------------------------------------

//		 if (this.controller != null)
//		 this.controller.destroy();

		// CIERRA EL ARCHIVO DE MOTOR DE RIESGO
		if (DatosDavivienda.STRATUS != null)
			DatosDavivienda.STRATUS.closeStratus();

	}
	// =======================================================================================================================

	// ===================================DOINGCONFIGURATIONS====================================================================================

	// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
	// INICIALES
	public void doingConfigurations() throws Exception {
		// Descomentariar la siguiente linea cuando se genere el JAR
		// HACE LA CONFIGURACIÓN REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
		// -----------------------------------------------------------------------------------------------------------------------
		// SELECIONA SI CONFIA EN LA CONTRATACIÓN

		String nombreAmbiente = SettingsRun.getGlobalData("AMBIENTE_PYME");

		switch (nombreAmbiente) {
		case "1":
		case "PROYECTOS":
			nombreAmbiente = "PROYECTOS";
			break;
		case "2":
		case "CONTENCION":
			nombreAmbiente = "CONTENCION";
			break;
		case "3":
		case "OBSOLESCENCIA":
			nombreAmbiente = "OBSOLESCENCIA";
			break;
		case "4":
		case "ONPREMISE":
			nombreAmbiente = "ONPREMISE";
			break;
		case "5":
		case "POST_NUBE":
			nombreAmbiente = "POST_NUBE";
			break;
		case "6":
		case "CONTENCION_NUBE":
			nombreAmbiente = "CONTENCION_NUBE";
			break;
		case "7":
		case "PROYECTOS_NUBE":
			nombreAmbiente = "PROYECTOS_NUBE";
			break;
		case "8":
		case "MEJORAS":
			nombreAmbiente = "MEJORAS";
			break;
		default:
			Reporter.reportEvent(Reporter.MIC_FAIL, "Opción no válida");
			break;
		}

		if (nombreAmbiente.isEmpty()) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "Nombre del ambiente seleccionado: " + nombreAmbiente);
		} else {
			Reporter.reportEvent(Reporter.MIC_HEADER, "Nombre del ambiente seleccionado: " + nombreAmbiente);
		}

		DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;

		contratacion = SettingsRun.getGlobalData("CONTRATACION");
		stratus = SettingsRun.getGlobalData("VALIDAR.STRATUS");

	}
	// =======================================================================================================================

	// ===========================================DOINGTEST===========================================================================

	// MÉTODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
	public void doingTest() throws Exception {
//		this.runAutomationBetweenHours(SettingsRun.getGlobalData("HoraDesVentana"), SettingsRun.getGlobalData("HoraTerVentana"));
		// -----------------------------------------------------------------------------------------------------------------------
		/*
		 * Datos Fijos Middle Login, estos datos se encuentran el archivo
		 * data.properties
		 */
		// numCli tipoDoc numDoc clave tipoTok datoTok
		DatosEmpresarial.loadLoginDataFija("0", SettingsRun.getGlobalData("MIDDLE.tipoDoc"),
				SettingsRun.getGlobalData("MIDDLE.numeroDeId"), SettingsRun.getGlobalData("MIDDLE.clavePersonal"),
				SettingsRun.getGlobalData("MIDDLE.tipoToken"), SettingsRun.getGlobalData("MIDDLE.numeroToken"));

		// -----------------------------------------------------------------------------------------------------------------------

		// Organiza los datos del cliente Middle con un array
		String[] datosLogin = DatosEmpresarial.getLoginData();
		// Reporta los datos del logeo
//			Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		if (contratacion.equals("SI") || contratacion.equals("SOLO"))
			Reporter.reportEvent(Reporter.MIC_INFO,
					"*** Datos de Logueo Middle: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");
		// numCli tipoDoc numDoc clave tipoTok datoTok
		numCliEmp = datosLogin[0];
		tipoDoc = datosLogin[1];
		numDoc = datosLogin[2];
		clave = datosLogin[3];
		tipoTok = datosLogin[4];
		datoTok = datosLogin[5];

		// -----------------------------------------------------------------------------------------------------------------------
		// Guarda los Datos del cliente Middle
		PageLoginPymes.datosMidell(numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok);
		// Guarda el Dato del Token Middle
		PageUsuariosEmpresa.datosMidellToke(datoTok);

		// -----------------------------------------------------------------------------------------------------------------------

		String msgError = null;
		// PARÁMETROS REQUERIDOS EN EL ARCHIVO EXCEL
		this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();
		this.empresa = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		this.producto = SettingsRun.getTestData().getParameter("Producto").trim();
		this.tiempoconsulta = SettingsRun.getTestData().getParameter("Tiempo de Consulta").trim();

		String clienteEmpresarial = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		String Idusuario = SettingsRun.getTestData().getParameter("Id usuario").trim();
		String tipoIdentificación = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		String tipoIDEmpresa = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		String numeroIDEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		// -----------------------------------------------------------------------------------------------------------------------

		/*
		 * Metodo se encarga de validar, que los datos obligatorios esten en el archivo
		 * a cargar
		 */
		// this.validarOCorregirData();

		// -----------------------------------------------------------------------------------------------------------------------

		String msgInicio = "";

		if (contratacion.equals("SI") || contratacion.equals("SOLO")) {
			this.servicio = "Consultas de Productos";
			msgInicio = "*** Cliente Empresarial: [" + clienteEmpresarial + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Identificación Usuario: [" + tipoIdentificación + "] - Tipo Identificación Usuario: ["
					+ Idusuario + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Servicio a contratar: [" + this.servicio + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Empresa a contratar: [" + this.empresa + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
			msgInicio = "*** Tipo Identificación Empresa: [" + tipoIDEmpresa + "] - Numero Identificación Empresa: ["
					+ numeroIDEmpresa + "]";
			Reporter.reportEvent(Reporter.MIC_INFO, msgInicio);
		}

		// *************************************************************************************************************************
		/*
		 * OPCIÓN SI DESEAS REALIZAR LA CONTRATACIÓN, 1RE REALIZA EL FLUJO DE
		 * CONTRATACIÓN DESDE MIDDLE Y PROCEDE A REALIZAR EL FLIJO EN FRONT
		 */
		if (contratacion.equals("SI")) {
			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());// Carga en que
																									// navegador se va
																									// realizar la
																									// prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT

			// -----------------------------------------------------------------------------------------------------------------------

			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {

				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);
				// -----------------------------------------------------------------------------------------------------------------------

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IGUAL A 1 SI NO ES IGUAL REALIZA LA
				 * VALIDACIÓN DE MIDDLE AL 2 CLIENTE
				 */
				// CONVIERTE EL NUMERO DE FIRMAS EN LA VARIABLE numfirm
				int contador = 1;

				/*
				 * VALIDA SI EL NUMERO DE FIRMAS ES IDIFERENTE A 1 REALIZA LA VALIDACIÓN DE
				 * MIDDLE AL 2 CLIENTE
				 */

				// -----------------------------------------------------------------------------------------------------------------------
				// REALIZA LA CONTRATACIÓN, EL FLUJO DE PYME MIDDLE

				controllerValiPymeMiddle.ValidacionMiddlefirmas(1);
				// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();

				// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
				if (msgError == null) {
					// LLAMA EL METODO DEL FLUJO LOGIN FRONT
					this.Front();
				}
			}
		}
		// *************************************************************************************************************************

		// *************************************************************************************************************************
		// OPCIÓN SI SELECIONA "Solo Contratar servicios" SOLO REALIZA EL FLUJO DE
		// MIDDLE
		if (contratacion.equals("SOLO")) {

			// INTENTA HACER EL LOGUEO
			this.pageLogin = new PageLoginPymes(this.navegador);// Carga en que navegador se va realizar la prueba
			msgError = this.pageLogin.loginMiddle(); // Método para hacer el logueo en el portal Middle Pyme.
			this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
														// Ambiente Middle o FRONT
// -----------------------------------------------------------------------------------------------------------------------
			// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
			if (msgError == null) {
				this.controllerValiPymeMiddle = new ControllerValiPymeMiddle(this.pageLogin);

// -----------------------------------------------------------------------------------------------------------------------
				// REALIZA EL FLUJO DE PYME MIDDLE
				controllerValiPymeMiddle.ValidacionMiddlefirmas(1);

// -----------------------------------------------------------------------------------------------------------------------
				// Cierra la sesion en la actual que se cuentra y procede al cierre del Browser.
				this.pageLogin.CerrarSesionMiddle();
			}
// -----------------------------------------------------------------------------------------------------------------------
		}
		// *************************************************************************************************************************

		// *************************************************************************************************************************
		// -----------------------------------------------------------------------------------------------------------------------
		// OPCIÓN SI SELECIONA "NO" SOLO REALIZA EL FLUJO DESDE EL PORTAL FRONT
		if (contratacion.equals("NO")) {
			// Realiza el Login en Stratus
			this.LoginStratus();
			this.Front();
		}

	}
// =======================================================================================================================

// ===========================================FRONT===========================================================================
	/**
	 * Método que llama logueo en el portal Front Pyme. Realiza el primer ingreso
	 */
	public void Front() throws Exception {

		// -----------------------------------------------------------------------------------------------------------------------
		String msgError = null;
		int intento = 0;
//		String msgInicio = "";
//		String msgInicio2 = "";
		// -----------------------------------------------------------------------------------------------------------------------
		// Datos Login front Login, estos datos se encuentran el archivo del carge DATA
		DatosEmpresarial.loadLoginData("Cliente Empresarial", "Tipo Identificación", "Id usuario",
				"Clave personal o CVE", "Tipo Token", "Semilla / Valor Estático / Celular");
		String[] datosLogin = DatosEmpresarial.getLoginData();
		Reporter.reportEvent(Reporter.MIC_INFO,
				"*** Datos de Logueo Front: [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");

// ***********************************************************************************************************************

		do {
// -----------------------------------------------------------------------------------------------------------------------
			// INTENTA HACER EL LOGUEO
			String evidenceDir = SettingsRun.RESULT_DIR;
			
//			this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());
			this.pageLogin = new PageLoginPymes(this.navegador, evidenceDir);
			msgError = this.pageLogin.loginFront();// Método para hacer el logueo en el portal front Pyme.
			this.pageLogin.selecionambienteClose("NO"); // Indicativo para el ambiente Front// Marca si esta en el//
			// Ambiente Middle o FRONT

// -----------------------------------------------------------------------------------------------------------------------
			// REINTENTA HACER EL LOGUEO SI SE PRESENTA MENSAJE DE ALERTA
			if (this.pageLogin.element(log30) != null) {
				String urlCargar = this.pageLogin.URL_FRONT;
				this.pageLogin.NavegadorFront(urlCargar);
				intento++;

			}
// -----------------------------------------------------------------------------------------------------------------------
			/*
			 * SI EL ACCESO ES DENEGADO POR SER RIESGO ALTO SE ESCRIBE EN EL ARCHIVO DE
			 * SALIDA SI EL ACCESO ES DENEGADO PERO ES RIEGO BAJO O MEDIO ESCRIBE ERROR EN
			 * EL ARCHIVO DE SALIDA
			 */

// -----------------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------------
		} while (this.pageLogin.element(log30) != null && intento < 3);
// ***********************************************************************************************************************
		// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
		if (msgError == null) {
			// Cierra la ventana actual del login de Front ya que va interatuar con la
			// ventana emergente
			boolean isWindowOpened = this.pageLogin.WaitForNumberOfWindos();
			if (isWindowOpened) {
				Reporter.reportEvent(Reporter.MIC_PASS, "La ventana emergente se abrió correctamente");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se abrió La ventana emergente");
				this.pageLogin.terminarIteracion();
			}

			this.pageLogin.closeCurrentBrowser();
// -----------------------------------------------------------------------------------------------------------------------
			// INTERATUA CON LA VENTANA EMERGENTE DE FRONT PYME
			this.pageLogin.changeWindow(this.pageLogin.accedioAlPortal());
// -----------------------------------------------------------------------------------------------------------------------
			// Obtiene el nombre de la empresa a selecionar
			this.pageOrigen = new PageOrigen(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------

			/*
			 * Intenta seleccionar la empresa. Retorna [null] si pudo hacer la selección, en
			 * caso contrario retorna mensajede error.
			 */

			msgError = this.pageOrigen.seleccionarEmpresa(this.empresa);
			// SI ES NULL EL MENSAJE DE ALERTA, SIGUE CON LAS DEMÁS VALIDACIONES
			if (msgError == null) {

				String tipoConsul = SettingsRun.getTestData().getParameter("Tipo de Consulta").trim();
				String descargarArch = SettingsRun.getTestData().getParameter("Formato de Descarga").trim();
				
				this.pageConsultasyExtractos = new PageConsultasyExtractos(this.pageLogin);
// -----------------------------------------------------------------------------------------------------------------------
				String tipoProduct = SettingsRun.getTestData().getParameter("Tipo producto a Consultar").trim();
				if (tipoConsul.equals("RESUMEN PRODUCTOS")) {
					this.pageConsultasyExtractos.ResumenDeprodutos();

				} else if (!tipoConsul.equals("RESUMEN PRODUCTOS")) {

					String consultaLis = SettingsRun.getTestData().getParameter("Hacer consulta desde Lista").trim();
					
					String numeroProducto = SettingsRun.getTestData().getParameter("Número producto a Consultar / numProducto-titular (filtro)").trim();

					// si es HA_CONS_DESDE_LISTA es true si es !HA_CONS_DESDE_LISTA false
					boolean listaconsulta = consultaLis.equals(HA_CONS_DESDE_LISTA);

					 
					String msg = this.pageConsultasyExtractos.SaldosConsolidado(navegador, producto,tipoConsul,listaconsulta,tipoProduct,numeroProducto);
					if (msg != null) {
						Reporter.reportEvent(Reporter.MIC_FAIL, msg);
						this.pageLogin.terminarIteracion();
					}

					Date fechaHoraTx = new Date();
					String tipoDocumento = SettingsRun.getTestData().getParameter("Tipo Identificación");
					String numeroDoc = SettingsRun.getTestData().getParameter("Id usuario").trim();

					// cambiar si la ruta del WinAppDriver es diferente
//					String winappdriverPath = "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
					// Método que permite abrir WinAppDriver, unicamente se debe modificar la
					// siguiente variable colocando
					// la bicación el archivo
					DXCUtil.startWinAppDriver();

					switch (tipoConsul) {
					case "SALDOS":
						this.pageSaldos = new PageSaldos(this.pageLogin);

						String tipoProductocon = SettingsRun.getTestData().getParameter("Tipo producto a Consultar");

						// Realiza la consulta de saldo de la cuenta desde la consulta de deltalle y
						// obtiene  los datos del saldo del Portal
						String msgConsultaSaldo[] = this.pageSaldos.ConsultaSaldo(tipoProductocon, numeroProducto);

						if (msgConsultaSaldo == null || msgConsultaSaldo.equals(" ")) {
							Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro información");
							this.pageLogin.terminarIteracion();
						}

						// Realiza la Consulta de detalle del saldo de la cuenta en Stratus y obtiene
						// los datos del saldo del detalle desde Stratus
						String tipoEmpresa = SettingsRun.getTestData().getParameter("Tipo ID Empresa");
						String numeroEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
						

						if (tipoProduct.contains("Rentalíquida") || tipoProduct.contains("Dafuturo")
								|| tipoProduct.contains("Daviplus") || tipoProduct.contains("Seguridad Bolivar")
								|| tipoProduct.contains("Superior") || producto.contains("FAMILIA FONDOS")) {
							tipoProduct = "FONDO";
						}
						if (DatosDavivienda.STRATUS != null && !tipoProductocon.equals("Leasing")&&!tipoProductocon.equals("leasing")) {
							
							String[] datosCtah = DatosDavivienda.STRATUS.consultarDatosPantallaSaldos(tipoEmpresa,numeroEmpresa, tipoProduct, numeroProducto);

							// Realizamos la comparacion de los saldo de stratus y la pantalla de pyme
							boolean sonIguales = true;
							
							//Obtiene el saldo correspondiente al tipo producto
							if (producto.contains("FAMILIA FONDOS") || tipoProduct.equals("FONDO")) {
								if (datosCtah[1].replace("$ ", "").trim().equals(msgConsultaSaldo[0].replace("$ ", "").replace(".", "").replace(",", ".").trim())) {
									String datoCtahSinEspacios = datosCtah[1].replace("$ ", "").trim();
									// Muestra las cadenas después de formatear
									Reporter.reportEvent(Reporter.MIC_INFO, "Dato consultado Portal Pyme["+ msgConsultaSaldo[0].replace("$ ", "").trim() + "]");
									Reporter.reportEvent(Reporter.MIC_INFO,"Dato consultado Stratus[" + datosCtah[1] + "]");

								} else {
									Reporter.reportEvent(Reporter.MIC_INFO, "Dato consultado Portal Pyme["+ msgConsultaSaldo[0].replace("$ ", "").trim() + "]");
									Reporter.reportEvent(Reporter.MIC_INFO,	"Dato consultado Stratus[" + datosCtah[1] + "]");
									sonIguales = false;
									break; // Puedes salir del bucle tan pronto como encuentres una diferencia
								}

							}
							if (tipoProduct.equals("Crediexpress")) {
								String datoCtahSinEspacios = datosCtah[2].replace("$ ", "").trim();
								// Muestra las cadenas después de formatear
								Reporter.reportEvent(Reporter.MIC_INFO, "Dato consultado Portal Pyme["+ msgConsultaSaldo[0].replace("$ ", "").trim() + "]");
								Reporter.reportEvent(Reporter.MIC_INFO,"Dato consultado Stratus[" + datosCtah[1] + "]");
							}
							
							if (!producto.contains("FAMILIA FONDOS") || !tipoProduct.equals("FONDO")) {

								for (int i = 0; i < datosCtah.length; i++) {
									// Elimina el carácter '$' y espacios de ambas cadenas antes de formatear
									//Dato stratus
									String datoCtahSinEspacios = "";
									if (tipoProduct.equals("Crediexpress"))
										datoCtahSinEspacios = datosCtah[2].replace("$ ", "").trim();
									else if (!tipoProduct.equals("Crediexpress"))
									 datoCtahSinEspacios = datosCtah[i].replace("$ ", "").trim();
									
									//Dato portal
									String msgConsultaSaldoSinEspacios = "";

									// Formatea los números
									String datoCtahFormateado = this.pageSaldos.formatearNumero(datoCtahSinEspacios);
									
									if (i < msgConsultaSaldo.length && msgConsultaSaldo[i] != null) { 
										msgConsultaSaldoSinEspacios = msgConsultaSaldo[i].replace("$ ", "").trim().replace(".", "").trim().replace(",", ".").trim();
									String msgConsultaSaldoFormateado = this.pageSaldos.formatearNumero(msgConsultaSaldoSinEspacios);

									// Muestra las cadenas después de formatear
									Reporter.reportEvent(Reporter.MIC_INFO,"Dato consultado Portal Pyme[" + i + "]: " + msgConsultaSaldoFormateado);
									Reporter.reportEvent(Reporter.MIC_INFO,"Dato consultado Stratus[" + i + "]: " + datoCtahFormateado);

									if (!datoCtahFormateado.equals(msgConsultaSaldoFormateado)) {
										Reporter.reportEvent(Reporter.MIC_INFO,"Diferencia en el campo " + (i + 1) + ":");
										Reporter.reportEvent(Reporter.MIC_INFO,"Dato consultado Portal Pyme[" + i+ "]: " + msgConsultaSaldoFormateado);
										Reporter.reportEvent(Reporter.MIC_INFO,"Dato consultado Stratus[" + i + "]: " + datoCtahFormateado);

										sonIguales = false;
										break; // Puedes salir del bucle tan pronto como encuentres una diferencia
									}
								}
								}

								// Mostrar el resultado
								if (sonIguales) {
									Reporter.reportEvent(Reporter.MIC_PASS, "Los datos son iguales.");
								} else {
									Reporter.reportEvent(Reporter.MIC_FAIL, "Los datos son diferentes.");

								}
							}
						}
						break;

					case "MOVIMIENTOS":

						// Listas para almacenar los movimientos
						List<String[]> movimientosPortal = new ArrayList<>();
						List<String[]> movimientosStratus = new ArrayList<>();
						
						String[] Mov = this.pageConsultasyExtractos.MovimientosPorProducto(tipoConsul,tipoProduct,tiempoconsulta,tipoProduct, numeroProducto,false,descargarArch);

						Reporter.write("===============Datos portal Pyme===============");

						for (String info : Mov) {
							Reporter.write("Movimiento portal: " + info);
							Reporter.write("---------------------------");

						}

						Reporter.write(" ");

						Reporter.write("===============Conversion Arreglo del portal===============");

						String Documento = null, fechaFormateada = null;
						double ValorTotalMovPy = 0, ValorChequeMovPy = 0;

						String[] re = null;

						String tipoProdc = SettingsRun.getTestData().getParameter("Tipo producto a Consultar").trim();

						if (!tipoProdc.contains("Daviplus")) {

							for (String data : Mov) {

								String[] campos = data.split(", ");

								// Asignar valores a las variables
								Date fech = DXCUtil.stringToDate(campos[0].trim(), "dd/MM/yyyy");
								SimpleDateFormat formatoSalida = new SimpleDateFormat("MMdd");
								fechaFormateada = formatoSalida.format(fech);
								Documento = DXCUtil.leftComplete(campos[1].trim(), 8, '0');
								String Descripcion = campos[2].trim();
								String Transaccion = campos[3].trim();
								String Oficina = campos[4].trim();
//						        String NitOriginador = campos[5].trim();
								String ValorCheque = campos[6].trim();
								ValorChequeMovPy = Double.parseDouble(ValorCheque);
								String ValorTotal = campos[7].trim();
								ValorTotalMovPy = Double.parseDouble(ValorTotal);

								re = this.pageConsultasyExtractos.Consulta(Descripcion, Transaccion, Oficina);

								Reporter.write("Movimiento: " + fechaFormateada + " " + Documento + "  " + re[0] + "   "
										+ re[1] + "   " + re[2] + "   " + BigDecimal.valueOf(ValorChequeMovPy) + "   "
										+ BigDecimal.valueOf(ValorTotalMovPy));

								String sjkds = fechaFormateada + " " + Documento + " " + re[0] + " " + re[1] + " "
										+ re[2] + " " + BigDecimal.valueOf(ValorChequeMovPy) + " "
										+ BigDecimal.valueOf(ValorTotalMovPy);

								movimientosPortal.add(sjkds.split(","));

							}
						}

						String fechaFormateadaCuen = null;
						String movimientocuen = null;
						double ValorTx = 0;
						if (tipoProdc.contains("Daviplus")) {

							for (String data : Mov) {

								String[] campos = data.split(", ");
								// Asignar valores a las variables
								Date fechaCu = DXCUtil.stringToDate(campos[0].trim(), "YYYY-DD-MM");
								SimpleDateFormat formatoSalida = new SimpleDateFormat("MMdd");
								fechaFormateadaCuen = formatoSalida.format(fechaCu);

								movimientocuen = campos[1].trim();
								String ValorTotal = campos[2].trim();
								ValorTx = Double.parseDouble(ValorTotal);

								Reporter.write("Movimiento: " + fechaFormateadaCuen + " " + movimientocuen + "   "
										+ BigDecimal.valueOf(ValorTx));

								String sjkds = fechaFormateadaCuen + " " + movimientocuen + "   "
										+ BigDecimal.valueOf(ValorTx);

								movimientosPortal.add(sjkds.split(","));

							}
						}

						Reporter.write(" ");
						if (DatosDavivienda.STRATUS != null) {
							String[] dates = this.pageConsultasyExtractos.today();

							// fechaInicial - Date que contiene la fecha de consulta inicial.
							Date fechaConsulta2 = DXCUtil.stringToDate(dates[0], "dd/MM/yyyy");
							// fechaFinal - Date que contiene
							Date fechaConsulta = DXCUtil.stringToDate(dates[1], "dd/MM/yyyy");

							String tipoProd = Stratus.getTipoCuenta(tipoProduct);

							// Datos del titular como un array de String
							String[] datosTitular = { tipoDocumento, numeroDoc };

							// Datos a consultar en la pantalla de movimientos en Stratus opsion [VENT]
							// valor Unidad
							String[] arrayVents = { MovimientoStratus.VENT_UNID };
							// Realiza la consulta en Stratus
							List<MovimientoStratus> datosCtadh2 = DatosDavivienda.STRATUS.getMovimientosEnRango(
									tipoProd, numeroProducto, arrayVents, fechaConsulta2, fechaConsulta, datosTitular);

							Collections.reverse(datosCtadh2);

							Reporter.write("===============Datos Stratus===============");

							double ValorTotalMov = 0, ValorChequeMovStartus = 0;

							String oficinaStratus = null, tipoStratus = null, mtvoStratus = null, talon = null,
									fecha = null;

							// Retorna los movimientos en Stratus
							for (MovimientoStratus movimiento : datosCtadh2) {
								String movToString = movimiento.toString();

								// Eliminar los prefijos "INDIC:" y "UNID:" y caracteres no deseados
								movToString = movToString.replaceAll("\\[INDIC: |UNID :|\\[|\\]", "");

								// Reemplazar " | " con ","
								movToString = movToString.replaceAll(" \\| ", ",");

								// Dividir la cadena en un arreglo de cadenas
								String[] datos = movToString.split(",");// Extraer los datos necesarios por posiciones
								fecha = datos[0];
//						String hora = datos[1];
//						String term = datos[2];
								talon = datos[3];
								tipoStratus = datos[4];
								oficinaStratus = datos[5];
								String valorStratus = datos[6];
								ValorTotalMov = Double.parseDouble(valorStratus);
								mtvoStratus = datos[7];
//						String fechamovimientoyuni = datos[8];
//						String fechaMov = DXCUtil.left(fechamovimientoyuni, 4);
//						String valorUnidadStratus = fechamovimientoyuni.replace(fechaMov, "");
								String chequeStratus = datos[9];
								ValorChequeMovStartus = Double.parseDouble(chequeStratus);

								Reporter.write("Movimiento: " + fecha + " " + talon + " " + mtvoStratus + "   "
										+ tipoStratus + "   " + oficinaStratus + "   "
										+ BigDecimal.valueOf(ValorChequeMovStartus) + "   "
										+ BigDecimal.valueOf(ValorTotalMov));

								String movr = fecha + " " + talon + " " + mtvoStratus + " " + tipoStratus + " "
										+ oficinaStratus + " " + BigDecimal.valueOf(ValorChequeMovStartus) + " "
										+ BigDecimal.valueOf(ValorTotalMov);

								if (movr.contains("0034") || movr.contains("0055")) {
									movimientosStratus.add(movr.split(","));
								}

							}

							Reporter.write(" ");
							Reporter.write("===============Comparación Datos===============");

							// Comparar movimientos uno a uno
							for (int i = 0; i < movimientosPortal.size() && i < movimientosStratus.size(); i++) {
								String[] movimientoPortal = movimientosPortal.get(i);
								String[] movimientoStratus = movimientosStratus.get(i);

								// Realizar la comparación de los datos aquí
								boolean sonIguales1 = Arrays.equals(movimientoPortal, movimientoStratus);

								if (sonIguales1) {
									Reporter.write("*** PASSED Movimiento igual: " + Arrays.toString(movimientoPortal)
											+ " = " + Arrays.toString(movimientoStratus));
									Reporter.write("------------------------------------");
								} else {
									if (movimientoPortal.length == movimientoStratus.length) {
										for (int j = 0; j < movimientoPortal.length; j++) {
											String as = movimientoPortal[j];
											String sad = movimientoStratus[j];

											String[] camposdi = as.split(" ");
											String[] camposdiStra = sad.split(" ");

											String fechaPortal = camposdi[0].trim();
											String documentoPortal = camposdi[1].trim();
											String descripcionPortal = camposdi[2].trim();
											String TipomovimientoPortal = camposdi[3].trim();
											String oficinaPortal = camposdi[4].trim();

											double valorChequePortal = Double.parseDouble(camposdi[5].trim());
											double valorTotalPortal = Double.parseDouble(camposdi[6].trim());

											String fechaStratus = camposdiStra[0].trim();
											String documentoStratus = camposdiStra[1].trim();
											String descripcionStratus = camposdiStra[2].trim();
											String tipomovimientoStratus = camposdiStra[3].trim();
											String oficinaStra = camposdiStra[4].trim();

											double valorChequeStratus = Double.parseDouble(camposdiStra[5].trim());
											double valorTotalStratus = Double.parseDouble(camposdiStra[6].trim());

											// Comparar campos uno a uno
											String msgva = null;
											if (!fechaPortal.equals(fechaStratus)) {

												msgva = "Fecha: ";
											}
											if (!documentoPortal.equals(documentoStratus)) {

												msgva = "Documento: ";
											}
											if (!descripcionPortal.equals(descripcionStratus)) {

												msgva = "Descripción: ";
											}
											if (!movimientoPortal.equals(movimientoStratus)) {

												msgva = "Tipo Movimiento";
											}
											if (!oficinaPortal.equals(oficinaStra)) {
												msgva = "Oficina: ";
											}
											if (valorChequePortal != valorChequeStratus) {
												msgva = "Valor Cheque: ";
											}
											if (valorTotalPortal != valorTotalStratus) {
												msgva = "Valor Total: ";
											}

											Reporter.reportEvent(Reporter.MIC_FAIL, "");
											Reporter.write("*** FAILED Movimiento diferente en el campo " + msgva
													+ Arrays.toString(movimientoPortal) + " = "
													+ Arrays.toString(movimientoStratus));

										}
									}
								}
							}
						}
						break;

					case "DESCARGA MOVIMIENTOS":
//					No lleva Stratus
						this.pageConsultasyExtractos.MovimientosPorProducto(tipoConsul,tipoProduct,tiempoconsulta,tipoProduct, numeroProducto, true,descargarArch);
						break;

					case "CHEQUERAS":
//					No lleva Stratus

						this.pageCheques = new PageCheques(this.pageLogin);
						String msgTxerror = this.pageCheques.Cheques(tipoProduct, numeroProducto);
						if (msgTxerror != null) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msgTxerror);
							this.pageLogin.terminarIteracion();
						}
						break;
					case "CHEQUES DEVUELTOS":
//					No lleva Stratus

						this.pageCheques = new PageCheques(this.pageLogin);
						this.pageCheques.Chequesdevuel(tipoProduct, numeroProducto);
						break;
					case "TX A TRAVÉS PORTAL":
						this.pageTxaTravezDelPortal = new PageTxaTravezDelPortal(this.pageLogin);
						String msgTxerror1 = this.pageTxaTravezDelPortal.TxDesdeElPortalDes(tipoProduct, numeroProducto, false);
						if (msgTxerror1 != null) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msgTxerror1);
							this.pageLogin.terminarIteracion();
						}
						this.LoginStratus();
						

						break;
					case "DESCARGA TX A TRAVÉS":
//					No lleva Stratus
						this.pageTxaTravezDelPortal = new PageTxaTravezDelPortal(this.pageLogin);
						String msgTxerror2 = this.pageTxaTravezDelPortal.TxDesdeElPortalDes(tipoProduct, numeroProducto,true);
						if (msgTxerror2 != null) {
							Reporter.reportEvent(Reporter.MIC_FAIL, msgTxerror2);
							this.pageLogin.terminarIteracion();
						}
						this.LoginStratus();
						break;
					default:
						break;
					}

				}

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgError);
				this.pageLogin.terminarIteracion();
			}

		}
		Reporter.reportEvent(Reporter.MIC_PASS, "Termina interacion");
		this.pageLogin.terminarIteracion();

	}

// =======================================================================================================================

// ===========================================[Stratus]===========================================================================

	/**
	 * Realiza el Login de Stratus de Forma general
	 * 
	 * @throws Exception
	 */
	public void LoginStratus() throws Exception {
		if (stratus.equals("SI")) {

			// Obtiene los datos desde el archivo Dataproperties.
			String strusu = SettingsRun.getGlobalData("STRATUS.usuario");
			String strpass = SettingsRun.getGlobalData("STRATUS.password");

			// Se comentan las siguientes 3 lineas para que no ejecute en stratus
			if (DatosDavivienda.STRATUS == null) {
				DatosDavivienda.STRATUS = new Stratus(strusu, strpass, "EMPRESAS");
				Reporter.reportEvent(Reporter.MIC_INFO, "Se cargan datos para logueo" + " de Stratus");
			}
		}
	}

	// ======================================================================================================================

}
