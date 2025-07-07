package dav.middlePymes;

import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.PageOrigen;
import dav.transversal.DatosEmpresarial;
import dav.transversal.DavUtil;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;
import launchTest.LaunchTestPyme;

import java.util.Properties;

import dav.divisas.PageInformesTransInternacionales;
import dav.middlePymes.*;

public class ControllerValiPymeMiddle {

	String tipoIdEm, clieEmpresa, servicio, idEmpresa, numIdUser, tipoIDUser, combo, nombreEmpre,numfirmas;

	PageLoginPymes pageLogPymes = null;
	PageAdministracion pageAdministracion = null;
	PageServicios pageServicios = null;
	PageUsuariosEmpresa pageUsuaEmp = null;
	PageOrigen pageOrigen = null;
	PageInformesTransInternacionales pageInfoTransInter = null;

	int[] iterationsPyme;

//=======================================================================================================================
	public ControllerValiPymeMiddle(PageLoginPymes pageParent) {
		pageAdministracion = new PageAdministracion(pageParent);
		pageServicios = new PageServicios(pageParent);
		pageUsuaEmp = new PageUsuariosEmpresa(pageParent);
		pageOrigen = new PageOrigen(pageParent);
		pageInfoTransInter = new PageInformesTransInternacionales(pageParent);
	}

//***********************************************************************************************************************		
	public void setIterationsPyme(int[] iterations) {
		this.iterationsPyme = iterations;
	}

	/**
	 * Metodo ValidacionMiddle: Llama los pageClases de Middle en el orden de
	 * ejecucion para la creacion de la parametria. validarClienteEmp
	 * asocEmpreClienEmp contratarServicio asoClienUser actualizarUsuarioEmpresa
	 * 
	 * @throws Exception
	 */
	public void ValidacionMiddle(String clieEmpresa, String tipoIdEm, String idEmpresa, String numIdUser,
			String tipoIDUser, String servicio, String combo, String nombreEmpre,String NumAprobaciones ) throws Exception {

		String[] msgAlert = pageAdministracion.validarClienteEmp(clieEmpresa, false);
		if (msgAlert[1] != null) {
			String[] msgAlert1 = pageAdministracion.asocEmpreClienEmp(clieEmpresa, tipoIdEm, idEmpresa, numIdUser,
					tipoIDUser, combo, NumAprobaciones, false);
			if (msgAlert1[0] != null && !msgAlert1[0].contains("inválido")) {

				String nbLink = pageServicios.getNbLinkServicio(servicio);
				String[] msgAlert2 = pageServicios.contratarServicio(clieEmpresa, tipoIdEm, idEmpresa, nbLink);

				if (msgAlert2[0] == null||!msgAlert2[0].equals("NO SE ENCONTRO EL SERVICIO")) {

					String msgAlert3 = pageUsuaEmp.actualizarUsuarioEmpresa(clieEmpresa, tipoIDUser, numIdUser,
							nombreEmpre);

					if (!msgAlert3.contains("exitosamente") || msgAlert3.contains("Debe seleccionar")) {

						Reporter.reportEvent(Reporter.MIC_FAIL, msgAlert3);
						this.pageOrigen.terminarIteracion();
					}

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro el servicio: " + servicio);
					this.pageOrigen.terminarIteracion();
				}

			} else {

				Reporter.reportEvent(Reporter.MIC_FAIL, "No se pudo asociar el cliente empresarial a: " + msgAlert1[0]);
				this.pageOrigen.terminarIteracion();
			}

		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, msgAlert[0]);
			this.pageOrigen.terminarIteracion();
		}

	}

	/**
	 * Metodo ValidacionMiddlefirmas: Parametriza los clientes registrado en el
	 * excel para el flujo de doblefirma, Realiza la validacion con la cantida de
	 * firmas a ejecutar.
	 * 
	 * @throws Exception
	 */
	public void ValidacionMiddlefirmas(int firmas) throws Exception {
		// OBTIENE LOS DATOS DEL CLIENTE EMPRESARIAL FRONT
		this.clieEmpresa = SettingsRun.getTestData().getParameter("Cliente Empresarial").trim();
		this.tipoIdEm = SettingsRun.getTestData().getParameter("Tipo ID Empresa").trim();
		this.idEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		if (SettingsRun.getTestData().parameterExist("Servicio")) {
			this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		}else {
			this.servicio = "Consultas de Productos";
		}
		this.nombreEmpre = SettingsRun.getTestData().getParameter("Nombre Empresa").trim();
		if (SettingsRun.getTestData().parameterExist("Combos")) {
			this.combo = SettingsRun.getTestData().getParameter("Combos").trim();
		}else {
			this.combo ="";
		}
		if (SettingsRun.getTestData().parameterExist("Números de Aprobaciones")) {
			this.numfirmas = SettingsRun.getTestData().getParameter("Números de Aprobaciones").trim();
		}else {
			this.numfirmas ="1";
		}

		// OBTIENE LOS DATOS DEL CLIENTE EMPRESARIAL FRONT DEPENDIENDO LA FIRMA SI ES
		// USUARIO 2 O 5 A FRIMAR
		if (firmas == 1) {
			this.numIdUser = SettingsRun.getTestData().getParameter("Id usuario").trim();
			this.tipoIDUser = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();

			String[] msgAlert = pageAdministracion.validarClienteEmp(clieEmpresa, false);
			if (msgAlert[1] != null) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Validacion Cliente empresa");
				String[] msgAlert1 = pageAdministracion.asocEmpreClienEmp(clieEmpresa, tipoIdEm, idEmpresa, numIdUser,
						tipoIDUser, combo, numfirmas, false);

				if (msgAlert1[0] != null) {
					String nbLink = pageServicios.getNbLinkServicio(servicio);
					String[] msgAlert2 = pageServicios.contratarServicio(clieEmpresa, tipoIdEm, idEmpresa, nbLink);

					if (!msgAlert2[0].equals("NO SE ENCONTRO EL SERVICIO") && !msgAlert2[0].contains("TimeOut")) {

						String msgAlert3 = pageUsuaEmp.actualizarUsuarioEmpresa(clieEmpresa, tipoIDUser, numIdUser,
								nombreEmpre);

						if (msgAlert3 != null && !msgAlert3.contains("exitosamente")) {

							Reporter.reportEvent(Reporter.MIC_FAIL, msgAlert3);
							this.pageOrigen.terminarIteracion();
						}

					} else {

						Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro el servicio: " + servicio);
						this.pageOrigen.terminarIteracion();
					}

				} else {

					Reporter.reportEvent(Reporter.MIC_FAIL,
							"No se pudo asociar el cliente empresarial a: " + msgAlert1[1]);
					this.pageOrigen.terminarIteracion();
				}

			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, msgAlert[0]);
				this.pageOrigen.terminarIteracion();
			}

		} else {
			
			this.numIdUser = SettingsRun.getTestData().getParameter("Id usuario " + firmas).trim();
			this.tipoIDUser = SettingsRun.getTestData().getParameter("Tipo Identificación " + firmas).trim();

			String msgAlert4 = pageUsuaEmp.actualizarUsuarioEmpresa(clieEmpresa, tipoIDUser, numIdUser, nombreEmpre);

			if (msgAlert4 != null && !msgAlert4.contains("exitosamente")) {

				Reporter.reportEvent(Reporter.MIC_FAIL, msgAlert4);
				this.pageOrigen.terminarIteracion();
			}
		}

	}
	
	

	public void ValidacionInformeTransInternacional() throws Exception {
		this.servicio = SettingsRun.getTestData().getParameter("Servicio").trim();
		this.idEmpresa = SettingsRun.getTestData().getParameter("Numero ID Empresa").trim();
		this.tipoIDUser = SettingsRun.getTestData().getParameter("Tipo Identificación").trim();
		this.numIdUser = SettingsRun.getTestData().getParameter("Id usuario").trim();
		
		pageInfoTransInter.InformeTransInter(this.servicio,this.tipoIDUser,this.numIdUser,this.idEmpresa);
	}
}
