package dav.middlePymes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dav.pymes.PageLoginPymes;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
import dxc.util.DXCUtil;

public class ControllerParamPyme {
	
	PageLoginPymes pageLogPymes= null;
	PageAdministracion pageAdministracion = null;
	PageServicios pageServicios = null;
	PageUsuariosEmpresa pageUsuaEmp = null;
	int[] iterationsPyme;

	// KEY = cliente empresarial, VALUE = Lista de las iteraciones que tienen el KEY
	Map<String,List<Integer>> dicClientes = new HashMap<String,List<Integer>>();
	// KEY = CLIENTE-TIPOID_EMP-NUMID_EMP, VALUE = Lista de las iteraciones que tienen el KEY
	Map<String,List<Integer>> dicCliEmpresa = new HashMap<String,List<Integer>>();
	// kEY = CLIENTE-TIPOID_EMP-NUMID_EMP, Value = Dictionary con los servicios y la Lista de las iteraciones
	Map<String,Map<String,List<Integer>>> dicCliEmpresaServ = new HashMap<String,Map<String,List<Integer>>>();
	// kEY = CLIENTE-TIPOID_USU-NUMID_USU, Value =  Lista de las iteraciones
	Map<String,List<Integer>> dicCliEmpresaUsuario = new HashMap<String,List<Integer>>();
	// kEY = CLIENTE, Value = Errores
	Map<String,String> dicErrores = new HashMap<String,String>();
	
//=======================================================================================================================
	public ControllerParamPyme(PageLoginPymes pageParent) {
		pageLogPymes = pageParent;
		pageAdministracion = new PageAdministracion(pageParent);
		pageServicios = new PageServicios(pageParent);
		pageUsuaEmp = new PageUsuariosEmpresa(pageParent);
	}
//***********************************************************************************************************************		
	public void setIterationsPyme(int[] iterations) {
		this.iterationsPyme = iterations;
	}
//***********************************************************************************************************************		
	public void configuracion() throws Exception {
		
		this.alistarDatos();
		List<Integer> listaIt;
		Map<String,List<Integer>> dicServicios;
		String msgError, numIdUser, tipoIdUser, firmas, nbEmpresa, combos;
		String[] arrayReturn ;
		String[] arrTemp, servicios, respServ;
//-----------------------------------------------------------------------------------------------------------------------
		// VALIDACION CLIENTE
		for (String cliEmpr : dicClientes.keySet()) {
			//@PEDRO CONSTRUCCION PARTE TX POR LO CUAL SE DEJA EN FALSE
			arrayReturn = pageAdministracion.validarClienteEmp(cliEmpr, false);
			//msgError = pageAdministracion.validarClienteEmp(cliEmpr, false);
			if (arrayReturn[0] != null) {
				listaIt = dicClientes.get(cliEmpr);
//				this.setearError(listaIt, arrayReturn[0]);
				dicErrores.put(cliEmpr, arrayReturn[0]);
				Reporter.reportEvent(Reporter.MIC_INFO, "Cliente [" + cliEmpr + "] : " + arrayReturn[0]);
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		// VALIDACIÓN RELACIÓN CLIENTE Y EMPRESA : KEY = CLIENTE-TIPOID_EMP-NUMID_EMP
		for (String keyCliEmp : dicCliEmpresa.keySet()) {
			arrTemp = keyCliEmp.split("-"); // 0-cliente, 1-tipoIdEmp, 2-numIdEmp
			if (dicErrores.containsKey(arrTemp[0]))
				dicErrores.put(keyCliEmp, dicErrores.get(arrTemp[0]));
			
			else { // SE PUEDE VALIDAR LA ASOCIACIÓN ENTRE CLIENTE Y EMPRESA
				listaIt = dicCliEmpresa.get(keyCliEmp);
				tipoIdUser = SettingsRun.getTestData().getParameterByExec("Tipo Documento", listaIt.get(0)).trim();
				numIdUser  = SettingsRun.getTestData().getParameterByExec("Número Documento", listaIt.get(0)).trim();
				combos = "SIN COMBOS";
				if (SettingsRun.getTestData().parameterExist("Combo Asignar"))
					combos = SettingsRun.getTestData().getParameterByExec("Combo Asignar", listaIt.get(0)).trim();
				firmas = "1";
				if (SettingsRun.getTestData().parameterExist("Número Firmas"))
					firmas = SettingsRun.getTestData().getParameterByExec("Número Firmas", listaIt.get(0)).trim();
				// MÉTODO DEBERÍA TRAER EL NOMBRE DE LA EMPRESA 

				String retorno[] = pageAdministracion.asocEmpreClienEmp(arrTemp[0], arrTemp[1], arrTemp[2], numIdUser, tipoIdUser, combos, firmas, false);
				
				if (retorno[0] != null) {
//					this.setearError(listaIt, retorno[0]);
					dicErrores.put(keyCliEmp, retorno[0]);
					Reporter.reportEvent(Reporter.MIC_INFO, "Cliente Empresa [" + keyCliEmp + "] : " + retorno[0]);
				}
//				else
//					this.setearEmpresa(listaIt, retorno[1]);
			}
		}
//-----------------------------------------------------------------------------------------------------------------------
		// VALIDA CONTRATACIÓN SERVICIO EN RELACIÓN CLIENTE Y EMPRESA
		for (String keyCliEmp : dicCliEmpresaServ.keySet()) {
			if (!dicErrores.containsKey(keyCliEmp)) {
				arrTemp = keyCliEmp.split("-"); // 0-cliente, 1-tipoIdEmp, 2-numIdEmp  
				dicServicios = dicCliEmpresaServ.get(keyCliEmp);
				servicios = DXCUtil.keysMapToArray(dicServicios);
				listaIt = dicServicios.get(servicios[0]); // EL DEL PRIMER SERVICIO				
				respServ = pageServicios.contratarServicio(arrTemp[0], arrTemp[1], arrTemp[2], servicios);

				for (int posArr = 0; posArr < servicios.length; posArr++) {
					String serv = servicios[posArr];
					if (respServ[posArr].contains(PageServicios.MSG_CONTRA_EXITO) || respServ[posArr].contains(PageServicios.MSG_SERVI_CONTRA))
						continue; // CONTINÚA CON EL SIGUIENTE ELEMENTO DEL FOR
					listaIt = dicServicios.get(servicios[posArr]);
//					this.setearError(listaIt, respServ[posArr]);
					dicErrores.put(keyCliEmp+"-"+serv, respServ[posArr]);
					Reporter.reportEvent(Reporter.MIC_INFO, "Cliente Empresa [" + keyCliEmp + "] + Contratando servicio ["
							+ serv + "] : " + respServ[posArr]);
				}
			}
		}
	//-----------------------------------------------------------------------------------------------------------------------		
		// VALIDACIÓN RELACIÓN CLIENTE Y USUARIO : KEY = CLIENTE-TIPOID_USU-NUMID_USU
		for (String keyCliEmp : dicCliEmpresaUsuario.keySet()) {
			arrTemp = keyCliEmp.split("-"); // 0-cliente, 1-tipoIdUser, 2-numIdUser
			
			if (dicErrores.containsKey(arrTemp[0]))
				dicErrores.put(keyCliEmp, dicErrores.get(arrTemp[0]));
				
			else { 
				listaIt = dicCliEmpresaUsuario.get(keyCliEmp);
				
				// SE PUEDE VALIDAR LA ASOCIACIÓN ENTRE CLIENTE Y USUARIO
				nbEmpresa = SettingsRun.getTestData().getParameterByExec("Nombre Empresa", listaIt.get(0)).trim();
				if (nbEmpresa.isEmpty())
					msgError = pageUsuaEmp.asoClienUser(arrTemp[0], arrTemp[1], arrTemp[2]);
				else
					msgError = pageUsuaEmp.actualizarUsuarioEmpresa(arrTemp[0], arrTemp[1], arrTemp[2], nbEmpresa);
				
				if (msgError != null) {
//					this.setearError(listaIt, msgError +"["+keyCliEmp+"]");
					dicErrores.put(keyCliEmp, msgError);
					Reporter.reportEvent(Reporter.MIC_INFO, "Cliente Empresa [" + keyCliEmp + "] : " + msgError);
				}else {
					String errorTemp  = SettingsRun.getTestData().getParameterByExec("Configuración Middle", listaIt.get(0)).trim();
					// VALIDACION DE ERROR YA SETTEADO, SE REALIZA YA QUE CUANDO HAY ERROR DE 1ER USUARIO, NO SE SOBREESCRIBA LA VALIDACION DEL 2DO
					if (!errorTemp.isEmpty()&& !errorTemp.equals("OK")) 
						continue;
//					this.setearConfOK(listaIt);
				}
			}
		}		
	}
//***********************************************************************************************************************
	public void alistarDatos() throws Exception {
		int iteration;
		String cliEmp, tipoDocEmp, numDocEmp, keyCliEmp, servicio, servEmpr, tipoDocUsu, numDocUsu;
		List<Integer> listaIt;
		Map<String,List<Integer>> dicServicios;
		for (int posIt = 0; posIt < iterationsPyme.length; posIt++) {
			iteration = iterationsPyme[posIt];
//-----------------------------------------------------------------------------------------------------------------------
			// ALISTA DICTIONARY DE CLIENTES
			cliEmp = SettingsRun.getTestData().getParameterByExec("Cliente Empresarial", iteration).trim();
			if (dicClientes.containsKey(cliEmp)) // YA EXISTE EL CLIENTE
				listaIt = dicClientes.get(cliEmp);
			else // NO EXISTE EL CLIENTE
				listaIt = new ArrayList<Integer>();
			listaIt.add(iteration);
			dicClientes.put(cliEmp, listaIt);
//-----------------------------------------------------------------------------------------------------------------------
			// ALISTA DICTIONARY DE RELACIÓN CLIENTE CON USUARIO
			tipoDocUsu = SettingsRun.getTestData().getParameterByExec("Tipo Documento", iteration).trim();
			numDocUsu  = SettingsRun.getTestData().getParameterByExec("Número Documento", iteration).trim();
			this.alimentarDictionary_ClienteEmpresaUsuario(tipoDocUsu, numDocUsu, cliEmp, iteration);
			if (SettingsRun.getTestData().parameterExist("Tipo Documento 2")) {
				tipoDocUsu = SettingsRun.getTestData().getParameterByExec("Tipo Documento 2", iteration).trim();
				numDocUsu  = SettingsRun.getTestData().getParameterByExec("Número Documento 2", iteration).trim();
				this.alimentarDictionary_ClienteEmpresaUsuario(tipoDocUsu, numDocUsu, cliEmp, iteration);
			}
//-----------------------------------------------------------------------------------------------------------------------
			// VERIFICA EXISTENCIA DE DATOS DE EMPRESA, SI NO VIENEN, ES PORQUE SÓLO SE DEBIÓ VALIDAR EXISTENCIA DEL
			// CLIENTE EMPRESARIAL (@PEDRO) Y DE USUARIO
			tipoDocEmp = SettingsRun.getTestData().getParameterByExec("Tipo Id Empresa", iteration).trim();
			numDocEmp  = SettingsRun.getTestData().getParameterByExec("Id Empresa", iteration).trim();
			if (tipoDocEmp.isEmpty() || numDocEmp.isEmpty())
				continue ;
//-----------------------------------------------------------------------------------------------------------------------
			// ALISTA DICTIONARY DE RELACIÓN CLIENTES CON EMPRESA
			keyCliEmp  = cliEmp + "-" + tipoDocEmp + "-" + numDocEmp;
			if (dicCliEmpresa.containsKey(keyCliEmp)) // YA EXISTE EL CLIENTE
				listaIt = dicCliEmpresa.get(keyCliEmp);
			else // NO EXISTE EL CLIENTE
				listaIt = new ArrayList<Integer>();
			listaIt.add(iteration);
			dicCliEmpresa.put(keyCliEmp, listaIt);
//-----------------------------------------------------------------------------------------------------------------------
			// ALISTA DICTIONARY DE RELACIÓN CLIENTE CON EMPRESA, ANIDANDO SERVICIOS (DICTIONARY)
			servicio = SettingsRun.getTestData().getParameterByExec("Servicio", iteration).trim();
			servEmpr = getServicioPyme(servicio);
			if (dicCliEmpresaServ.containsKey(keyCliEmp)) { // YA EXISTE EL CLIENTE
				dicServicios = dicCliEmpresaServ.get(keyCliEmp);
				if (dicServicios.containsKey(servEmpr))
					listaIt = dicServicios.get(servEmpr);
				else
					listaIt = new ArrayList<Integer>();
			}
			else { // NO EXISTE EL CLIENTE
				dicServicios = new HashMap<String,List<Integer>>();
				listaIt = new ArrayList<Integer>();
				}
			listaIt.add(iteration);
			dicServicios.put(servEmpr, listaIt);
			dicCliEmpresaServ.put(keyCliEmp, dicServicios);

		}
	}
	
//***********************************************************************************************************************
	/**
	 * Método que alimenta la información para relación cliente, usuario.<br>
	 * Se hace en método porque puede ser afectado por 2 usarios diferentes.
	 */
	private void alimentarDictionary_ClienteEmpresaUsuario(String tipoDocUsu, String numDocUsu, String CliEmp, int iteration) {
		if (tipoDocUsu.isEmpty() || numDocUsu.isEmpty())
			return; // NO ALIMENTA EL DICTIONARY
		List<Integer> listaIt;
		String keyCliEmpUsu = CliEmp + "-" + tipoDocUsu + "-" + numDocUsu;
		if (dicCliEmpresaUsuario.containsKey(keyCliEmpUsu)) // YA EXISTE EL CLIENTE  CON EL USUARIO
			listaIt = dicCliEmpresaUsuario.get(keyCliEmpUsu);
		else 
			listaIt = new ArrayList<Integer>();
		listaIt.add(iteration);
		dicCliEmpresaUsuario.put(keyCliEmpUsu, listaIt);
	}
//***********************************************************************************************************************
//	private void setearError(List<Integer> listaIteraciones, String msgError) throws Exception {
//		for (int posLista = 0; posLista < listaIteraciones.size(); posLista++) {
//			SettingsRun.getTestData().getParameterByExec("Configuración Middle", listaIteraciones.get(posLista), msgError);
//		}
//	}
//***********************************************************************************************************************
//	private void setearEmpresa(List<Integer> listaIteraciones, String nbEmpresa) throws Exception {
//		for (int posLista = 0; posLista < listaIteraciones.size(); posLista++) {
//			SettingsRun.getTestData().getParameterByExec("Nombre Empresa", listaIteraciones.get(posLista), nbEmpresa);
//		}
//	}
//	private void setearConfOK(List<Integer> listaIteraciones) throws Exception {
//		for (int posLista = 0; posLista < listaIteraciones.size(); posLista++) {
//			SettingsRun.getTestData().getParameterByExec("Configuración Middle", listaIteraciones.get(posLista), "OK");
//		}
//	}
	
	public void setDiscCliente(Map<String,List<Integer>> dicClientesMap,Map<String,List<Integer>> dicCliEmpresaMap , Map<String, Map<String, List<Integer>>> dicCliEmpresaServMap ,Map<String,List<Integer>> dicCliEmpresaUsuarioMap ) {
		this.dicClientes=dicClientesMap;
		this.dicCliEmpresa=dicCliEmpresaMap;
		this.dicCliEmpresaServ=dicCliEmpresaServMap;
		this.dicCliEmpresaUsuario=dicCliEmpresaUsuarioMap;
	}
//***********************************************************************************************************************	
//***********************************************************************************************************************
	public void destroy() {
		
		if (pageLogPymes != null)
			pageLogPymes.closeSession();
	}
//***********************************************************************************************************************
	/**
	 * Metodo retorna el nombre de los servicios
	 * @param servicio
	 * @return El nombre del servicio
	 */
	private String getServicioPyme(String servicio) {
		String servicioRetorno = "Transferencia NIT Propio";
		switch (servicio) {
		
		case "Transferencia cuenta inscrita": 
			servicioRetorno = "Transferencias Cuenta Inscrita";
			break;
		case "Transferencia cuenta no inscrita": 
			servicioRetorno = "Transferencias Cuenta No Inscrita";
			break;
		case "Pago Nómina": 
			servicioRetorno = "Pago de Nóminas";
			break;
		case "Pago Proveedores":
			servicioRetorno = "Pago a Proveedores";
			break;
		case "Pago Servicios":
			servicioRetorno = "Pago de Servicios";
				break;
			}
			return servicioRetorno;	
		}
	
}