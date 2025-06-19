package launchTest;
import java.util.Properties;

import org.openqa.selenium.By;

import dav.Comercios.ControllerComercios;
import dav.Informes.ControllerInformes;
import dav.Libranza.ControllerLibranza;
import dav.middlePymes.ControllerValiPymeMiddle;
import dav.middlePymes.PageUsuariosEmpresa;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloTx.PageAdminParametros;
import dav.pymes.moduloTx.PageOrigen;
import dav.transversal.DatosEmpresarial;
import dav.transversal.MotorRiesgo;
import dxc.dav.library.reporting.EvidencePdfFile;
import dxc.execution.BaseTestNG;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class Launch_Informes extends BaseTestNG{





		// =======================================================================================================================
		// Page & Crontroller
		PageLoginPymes pageLogin = null;
		PageOrigen pageOrigen = null;
		PageUsuariosEmpresa pageUsuariosEmpresa = null;
		PageAdminParametros pageAdminParametros = null;
		ControllerValiPymeMiddle controllerValiPymeMiddle = null;
		ControllerInformes controllerInformes = null;


		Properties info;

		// =======================================================================================================================

		String contratacion = null;

		String tipoPrueba, desde_el_Detalle, servicio, riesgoBc, riesgoEfm, riesgo, userAgent, navegador, empresa, informe;
		String numCliEmp, tipoDoc, numDoc, clave, tipoTok, datoTok;
		String na = "N/A";
		// =======================================================================================================================
		String tipoIdEm, clieEmpresa, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre;
		// =======================================================================================================================




		// ================================LAUNCHDATA=======================================================================================

		public void launchData() { // DATOS DEL LANZAMIENTO
//			SettingsRun.EXEC_CLASS = this.getClass().getSimpleName(); // NOMBRE CARPE 5656TA DE EVIDENCIAS
//			Evidence.siTomarla(Evidence.FORMATO_EXCEL);// INDICAR LA TOMA DE EVIDENCIAS Y EL FORMATO
			Reporter.initializeEvidenceType(new EvidencePdfFile());
			SettingsRun.DEFAULT_HEADER = 4; // EL ARCHIVO DE DATOS EL HEADER EST� EN LA FILA 4 : USARLO SI EL HEADER NO EST�									// EN 1

			// -----------------------------------------------------------------------------------------------------------------------

			// PAR�METROS REQUERIDOS EN LA HOJA DE DATOS GLOBAL PARA EL LAUNCH QUE SE EST�
			// HACIENDO
			SettingsRun.ARRAY_DATA_PARAMS = new String[] { "Selecci�n" };

			// -----------------------------------------------------------------------------------------------------------------------
	 
		}
		// =======================================================================================================================

		// =======================================================================================================================

		// ====================================LAUNCHCLOSE===================================================================================

			public void launchClose() { // CIERRE DEL LANZAMIENTO
		//
		//// -----------------------------------------------------------------------------------------------------------------------

//				if (this.pageLogin != null)
//					this.pageLogin.closeAllBrowsers();
		//// -----------------------------------------------------------------------------------------------------------------------

			}
		// =======================================================================================================================

		// ===================================DOINGCONFIGURATIONS====================================================================================

		// SOLICITUD DATOS GLOBALES, CONFIGURACIONES INICIALES Y/O VALIDACIONES
		// INICIALES
		public void doingConfigurations() throws Exception {

			// HACE LA CONFIGURACI�N REQUERIDA PREVIA REQUERIDA PARA TODAS LAS ITERACIONES
			// -----------------------------------------------------------------------------------------------------------------------
			// SELECIONA SI CONFIA EN LA CONTRATACI�N

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
				Reporter.reportEvent(Reporter.MIC_FAIL, "Opci�n no v�lida");
				break;
			}

			if (nombreAmbiente.isEmpty()) {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Nombre del ambiente seleccionado: Portal" + nombreAmbiente);
			} else {
				Reporter.reportEvent(Reporter.MIC_HEADER, "Nombre del ambiente seleccionado: Portal" + nombreAmbiente);
			}

			DatosEmpresarial.AMBIENTE_TEST = nombreAmbiente;

			contratacion = SettingsRun.getGlobalData("CONTRATACION");

		}
		// =======================================================================================================================

		// ===========================================DOINGTEST===========================================================================

		// M�TODO QUE ENMARCA LAS PRUEBAS A REALIZAR POR CADA LANZAMIENTO
		public void doingTest() throws Exception {
//				this.runAutomationBetweenHours(info.getProperty("HoraDesVentana"), info.getProperty("HoraTerVentana"));

			// -----------------------------------------------------------------------------------------------------------------------
			/*
			 * Datos Fijos Middle Login, estos datos se encuentran el archivo
			 * data.properties
			 */
			// numCli tipoDoc numDoc clave tipoTok datoTok
			DatosEmpresarial.loadLoginDataFija("0", SettingsRun.getGlobalData("MIDDLE.tipoDoc"),
					SettingsRun.getGlobalData("MIDDLE.numeroDeId"),
					SettingsRun.getGlobalData("MIDDLE.clavePersonal"),
					SettingsRun.getGlobalData("MIDDLE.tipoToken"),
					SettingsRun.getGlobalData("MIDDLE.numeroToken"));
	 
			// -----------------------------------------------------------------------------------------------------------------------

			// Organiza los datos del cliente Middle con un array
			String[] datosLogin = DatosEmpresarial.getLoginData();
			// Reporta los datos del logeo
//				Reporter.write("Datos de Logueo [" + DXCUtil.arrayToString(datosLogin, " - ") + "]");

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
			// PAR�METROS REQUERIDOS EN EL ARCHIVO EXCEL
			this.navegador = SettingsRun.getTestData().getParameter("Navegador").trim();

			// -----------------------------------------------------------------------------------------------------------------------
			
				this.pageLogin = new PageLoginPymes(this.navegador, Evidence.getNbEvidenceDirectory());// Carga en que navegador se va realizar la prueba
				msgError = this.pageLogin.loginMiddle(); // M�todo para hacer el logueo en el portal Middle Pyme.
				this.pageLogin.selecionambienteClose("SI");// Indicativo para el ambiente middle// Marca si esta en el
															// Ambiente Middle o FRONT
 
				// -----------------------------------------------------------------------------------------------------------------------

				// SI ES NULL EL MENSAJE DE ALERTA INGRESA AL LOGIN
				if (msgError == null) {
					
					this.controllerInformes = new ControllerInformes(this.pageLogin);
					this.controllerInformes.informesMiddle();					

				}
 



		// =======================================================================================================================
	

}
}
