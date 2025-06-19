package dav.middlePymes;

import java.util.Calendar;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dxc.library.reporting.Evidence;
//import dxc.execution.Evidence;
import dxc.util.DXCUtil;

public class PageAprobacionesApp extends PageInicioMiddle {
	
	// LOCATORS Y EXPRESIONES PARA ENCONTRAR LOS OBJETOS EN MIDDLE PYME
	By locFechaInicial		= By.xpath("//input[@name=\'ctl00$cphCuerpo$txtFechaInicial\' and @id=\'cphCuerpo_txtFechaInicial\']"); // tagName = input
	By locFechaFinal 		= By.id("cphCuerpo_txtFechaFinal"); // tagName = input
	By locEstado 			= By.id("cphCuerpo_dropEstado");// tagName = select
	By locPerfilUser 		= By.id("cphCuerpo_dropPerfilUsuario"); // tagName = select
	By locNombrePro 		= By.id("cphCuerpo_txtNombreProceso"); // tagName = input
	By locProductoOri 		= By.id("cphCuerpo_dropProductoOrigen"); // tagName = select
	By locTipoIdEmpresa 	= By.id("cphCuerpo_dropTipoIdentificacionEmpresa"); // tagName = select
	By locNumIdEmpresa		= By.id("cphCuerpo_txtNumIdentificacionEmpresa"); // tagName = input
	By locMetodoAprobacion	= By.id("cphCuerpo_ddlTipoAutenticacion"); // tagName = select
	By locNumProceso 		= By.id("cphCuerpo_txtProceso"); // tagName = input
	By locNumCliEmp 		= By.id("cphCuerpo_txtNumClienteEmpresarial"); // tagName = input
	By locBtnBuscarProceso	= By.xpath("//input[@id='cphCuerpo_btBuscar' and @value='Buscar Procesos']");
	By locMensajeAlerta		= By.id("lblMasterAlerta");
	By locTablaAprobaciones	= By.id("cphCuerpo_gvAprobaciones");
	By locGrillaTabla		= By.id("cphCuerpo_panelGrilla");
	String locNumeroProceso	= "//td[text()='NUMCTA']//preceding-sibling::td[text()='HORA']//preceding-sibling::td[text()='FECHA']//preceding-sibling::td[text()='SERVICIO']//preceding-sibling::td[2]";
							   //td[text()='550099800035273']//preceding-sibling::td[text()='14:11']//preceding-sibling::td[text()='09/08/2021']//preceding-sibling::td[text()='NOMI']//preceding-sibling::td[2]
	String locBtnSiguiente	= "//a[@href=(34)javascript:__doPostBack('ctl00$cphCuerpo$lnkSiguiente','')(34)]"; //a[@href="javascript:__doPostBack('ctl00$cphCuerpo$lnkSiguiente','')"]
	By LocListPaginacionPro	= By.id("cphCuerpo_ddlPaginas");
	String locColEstado		= "//td[text()='PROCESO']//following-sibling::td[text()='FECHA']//following-sibling::td[text()='FECHA']//following-sibling::td[5]";

//=======================================================================================================================
	/**
	 * Este es el único constructor que tiene esta [BasePageWeb] ya que por sí sola
	 * no puede existir, debe depender de otra [BasePageWeb] que ya haya sido
	 * abierta y que direccione a ésta.
	 */
	public PageAprobacionesApp(PageLoginPymes parentPage) {
		super(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de ingresar el dato fecha inicial, este debe ser un String en formato "DD/MM/AAAA".
	 */
	public String ingresarFechaInicial(String fecha) {
		//DXCUtil.wait(3);
		WebElement objFecha = this.element(locFechaInicial);
		if (objFecha == null)
			return "No se identificó el objeto fecha inicial por lo que no se ingresó ningún dato en este campo.";
		
		this.write(objFecha, fecha);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Metodo se encarga de ingresar el dato fecha Final, este debe ser un String en formato "DD/MM/AAAA".
	 */
	public String ingresarFechaFinal(String fecha) {
		WebElement objFecha = this.element(locFechaFinal);
		if (objFecha == null)
			return "No se identificó el objeto fecha final por lo que no se ingresó ningún dato.";
		
		this.write(objFecha, fecha);
		return null;
	}	
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de seleccionar de la lista el valor del estado de la transacción.
	 */
	public String seleccionarEstado (String valor) {
		// SELECCIONA EL TIPO DE IDENTIFICACIÓN
		String msgError = this.selectListItem(this.element(locEstado), valor);
		if (!msgError.isEmpty())
			return "Lista 'Estado' : " + msgError;
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de seleccionar de la lista el valor del Perfil de usuario a consultar.
	 */
	public String seleccionarPerfil (String valor) {
		// SELECCIONA EL TIPO DE IDENTIFICACIÓN
		String msgError = this.selectListItem(this.element(locPerfilUser), valor);
		if (!msgError.isEmpty())
			return "Lista 'Perfil' : " + msgError;
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de ingresar el dato Nombre proceso.
	 */
	public String ingresarNombreProceso(String proceso) {
		WebElement objProceso = this.element(locNombrePro);
		if (objProceso == null)
			return "No se identificó el objeto para nombre proceso, por lo que no se ingresó ningún dato.";
		
		this.write(objProceso, proceso);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de seleccionar de la lista el tipo de producto origen de la transaccion a consultar.
	 */
	public String seleccionarProdOrigen (String valor) {
		// SELECCIONA EL TIPO DE IDENTIFICACIÓN
		String msgError = this.selectListItem(this.element(locProductoOri), valor);
		if (!msgError.isEmpty())
			return "Lista 'Producto Origen' : " + msgError;
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de seleccionar de la lista el tipo de documento de la empresa a consultar.
	 * @throws Exception 
	 */
	public String seleccionarTipoIdEmp (String valor) {
		// SELECCIONA EL TIPO DE IDENTIFICACIÓN
		String msgError = this.selectListItem(this.element(locTipoIdEmpresa), valor);
		if (!msgError.isEmpty())
			return "Lista 'Tipo Id Empresa' : " + msgError;
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de ingresar el Número de Identificacion de la empresa a consultar.
	 */
	public String ingresarnumIdEmp(String numId) {
		WebElement objId = this.element(locNumIdEmpresa);
		if (objId == null)
			return "No se identificó el objeto para Número Id Empresa, por lo que no se ingresó ningún dato.";
		
		this.write(objId, numId);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de seleccionar de la lista el metodo de aprobación de la transaccion a consultar.
	 */
	public String seleccionarMetodoAprobacion (String valor) {
		// SELECCIONA EL TIPO DE IDENTIFICACIÓN
		String msgError = this.selectListItem(this.element(locMetodoAprobacion), valor);
		if (!msgError.isEmpty())
			return "Lista 'Método de Aprobación' : " + msgError;
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Método se encarga de ingresar el dato Número de proceso si se conoce.
	 */
	public String ingresarNumProceso(String proceso) {
		WebElement objProceso = this.element(locNumProceso);
		if (objProceso == null)
			return "No se identificó el objeto para numero de proceso, por lo que no se ingresó ningún dato en este campo.";
			
		this.write(objProceso, proceso);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Este Metodo se encarga de ingresar el dato Número de cliente empresarial.
	 */
	public String ingresarNumClienteEmpresarial(String numCli) {
		WebElement objProceso = this.element(locNumCliEmp);
		if (objProceso == null)
			return "No se identifico el objeto para numero de cliente empresarial, por lo que no se ingreso ningún dato en este campo.";
			
		this.write(objProceso, numCli);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * Da click en el botón que permite visualizar el resultado del filtro a realizar.
	 */
	public void clickBuscarProceso() {
		WebElement objBtBuscarProceso = this.element(locBtnBuscarProceso);
		if (objBtBuscarProceso != null) this.click(objBtBuscarProceso);
	}
//***********************************************************************************************************************
	/**
	 * Método que busca una respuesta a la consulta previamente realizada en aprobaciones.<br>
	 * Retorna mensaje de error de encontrarlo, * si encuentra tabla de resultados retorna vacio.
	 */
	private String resultadoBusqueda() throws Exception {
		WebElement objMsgError = null;
		WebElement objTablaRes = null;
		//Espera Hasta que se muestre un mensaje o la tabla de resultados.
		do {
			DXCUtil.wait(1);
			objMsgError = this.element(locMensajeAlerta);
			objTablaRes = this.element(locTablaAprobaciones);
		} while (objMsgError == null && objTablaRes == null);
		if (objMsgError != null)
			if (isDisplayed(objMsgError) && isEnabled(objMsgError)) {
				//Reporter.writeTitle("Se identifico el mensaje de Error y es visible y esta habilitado");
//				Evidence.save("ErrorConsulta", this);
				Evidence.save("ErrorConsulta");
				return objMsgError.getText();
			} //else Reporter.writeTitle("Se identifico el mensaje de Error pero NO es visible");
		if (objTablaRes != null)
			if (isDisplayed(objTablaRes) && isEnabled(objTablaRes)) {
				//WebElement objTablaDiv = this.element(locGrillaTabla);
				//Evidence.saveFullElement("Prueba", this, objTablaDiv, true, false);
				return "";
			} //else Reporter.writeTitle("Se identifico la tabla de resultado pero NO es visible");
		// locMensajeAlerta
		Evidence.saveFullPage("Busqueda-Transacciones", this);
		
		return "No se presentó ni la tabla de resultados, ni mensaje al consultar los procesos, valide la información";
	}
//***********************************************************************************************************************
	/**
	 * Método que realiza la busqueda del número de proceso correspondiente a una transaccion previamente realizada, para ello requiere:<br>
	 * fechaIni = dato tipo String que debe estar en formato "DD/MM/AAAA", Este dato se diligencia en fecha inicial y final.<br>
	 * tipoIdEmp = Dato tipo String Que indica el tipo de documento de la empresa que realizo la transacción.<br>
	 * numIdEmp = Dato tipo String Que indica el número de documento de la empresa que realizo la transacción.<br>
	 * numCliEmp = Dato tipo String Que indica el número de cliente empresarial con que se realizo la transacción.<br>
	 * tipoTran = Dato tipo String Que indica el servicio a transar tal cual como se muestra en la data.<br>
	 * numProducto = Dato tipo String Que indica el numero de cuenta origen de la que se transo.<br>
	 * horaTransaccion = Dato tipo String Que indica la hora en que se realizo la transacción, para realizar la busqueda.<br>
	 * valida si se presenta mensaje de error o si muestra la tabla de resultados y en esta confirma el numero del proceso,
	 * si lo identifica retorna el numero, de lo contrario retorna vacio.
	 * @throws Exception 
	 */
	public String consultarNumProcesoPendiente (String fechaIni, String tipoIdEmp, String numIdEmp, String numCliEmp,
			String tipoTran, String numProducto, String horaTransaccion) throws Exception {
		
		// Carga los datos requeridos para la busqueda
		this.ingresarFechaInicial(fechaIni);
		this.ingresarFechaFinal(fechaIni);
		this.seleccionarEstado("PENDIENTE DE APROBAR SIGUIENTE APODERADO");
		this.seleccionarTipoIdEmp(tipoIdEmp);
		this.ingresarnumIdEmp(numIdEmp);
		this.ingresarNumClienteEmpresarial(numCliEmp);
//		Evidence.save("Consulta-proceso", this);
		Evidence.save("Consulta-proceso");
		// Da clic en el botón buscar procesos
		this.clickBuscarProceso();
		// Valida el resultado de la busqueda.
		String msgError = this.resultadoBusqueda();
		String numProceso = "";
		if (!msgError.isEmpty()) {
//			Evidence.save("Error-Consulta-proceso", this);
			Evidence.save("Error-Consulta-proceso");
			return msgError;
		}
		else { // si existe la tabla de resultados por lo que se validará si se identifica el mumero de proceso
			String servicio = serviciosBaseACodigo(tipoTran);
			//Reporter.writeTitle("servicio identificado como ["+ servicio +"]");
			numProceso = obtenerNumProceso(fechaIni, servicio, numProducto, horaTransaccion);
//			Evidence.save("Error-Consulta-proceso", this);
			Evidence.save("Error-Consulta-proceso");
		}
		return numProceso;
	}
//***********************************************************************************************************************
	/**
	 * Método que retorna el acronimo del servicio indicado en el String servicioBase.
	 */
	public String serviciosBaseACodigo (String servicioBase) {
		//convierte el servicio base recibido en mayusculas y sin acentos (tildes)
 		String servicio = DXCUtil.removeAccents(servicioBase).toUpperCase();
		//Reporter.writeTitle("servicio cargado ["+ servicio +"]");
		if (DXCUtil.containsIgnoreCaseAndAccents(servicio, "nomina")) {
			return "NOMI";
		}else if (DXCUtil.containsIgnoreCaseAndAccents(servicio, "Servicios")) {
			return "PASE";
		}else if (DXCUtil.containsIgnoreCaseAndAccents(servicio, "Proveedores")) {
			return "PROV";
		}else if (DXCUtil.containsIgnoreCaseAndAccents(servicio, "otras") && DXCUtil.containsIgnoreCaseAndAccents(servicio, "cuentas") ) {
			return "TRCI/TRCN";//TRANSFERENCIAS_CUENTA_INSCRITA;
		}else if (DXCUtil.containsIgnoreCaseAndAccents(servicio, "otros") && DXCUtil.containsIgnoreCaseAndAccents(servicio, "Bancos") ) {
			return "TRCI/TRCN";//TRANSFERENCIAS_CUENTA_NO_INSCRITA;
		}else if (DXCUtil.containsIgnoreCaseAndAccents(servicio, "Transferencia") && DXCUtil.containsIgnoreCaseAndAccents(servicio, "entre") ) {
			return "TRMN";
		}else  return "";
	}
//***********************************************************************************************************************
	/**
	 * Metodo que retorna el número de proceso una vez realizada la consulta, ya debe encontrarse la tabla de resultados
	 * para realizar la validación, de lo contrario retorna vacio.
	 */
	public String obtenerNumProceso (String fecha, String servicio, String numProducto, String hora) throws Exception {
		
		String locNumeroProceso1 = locNumeroProceso.replace(
				"NUMCTA", DXCUtil.right(numProducto, 15)).replace("FECHA", fecha);
		
		String[] servs = servicio.split("/");
		String[] horaValidar = {"","",""};
		int[] addMinutes = { 0, -1, 1 };
		Date fechaCompleta = DXCUtil.stringToDate(fecha + " " + hora + ":00", "dd/mm/yyyy hh:mm:ss");
		for (int i = 0; i < addMinutes.length; i++)
			horaValidar[i] = DXCUtil.hourToString(DXCUtil.dateAdd(fechaCompleta, Calendar.MINUTE, addMinutes[i]), "HH:mm");
		WebElement objNumProceso = null;
		// Se remplaza "(34)" por su equivalente en ascci que son las comillas ya que al dejar el xpath con comilla sensilla no encontraba el objeto
		locBtnSiguiente = locBtnSiguiente.replace("(34)", Character.toString((char)34));
		WebElement objsiguiente = this.element(By.xpath(locBtnSiguiente));
		do {
			objsiguiente = this.element(locBtnSiguiente);
			for(int j = 0; j < servs.length; j++) {
				for(int i = 0; i < horaValidar.length; i++) {
					objNumProceso = this.element(By.xpath(locNumeroProceso1.replace("SERVICIO", servs[j]).replace("HORA", horaValidar[i])));
					
					if (objNumProceso != null) {
						Evidence.saveFullPage("Proceso-" + objNumProceso.getText(), this);//save("Proceso-" + objNumProceso.getText(), this);
						WebElement objTablaDiv = this.element(locGrillaTabla);
						Evidence.saveFullElement("TablaProceso-" + objNumProceso.getText(), this, objTablaDiv, true, false);
						return objNumProceso.getText();
					}
				}
			}
			if (objNumProceso == null) {
				String optionOld = getItemSelected(LocListPaginacionPro);
				String optionNew = getItemSelected(LocListPaginacionPro);
				if (objsiguiente != null) {
					objsiguiente.click();//Da click en el link siguiente para validar si en la siguiente consulta se encuentra el numero de proceso 
					do {//espera hasta que cambie el resultado de la consulta de la tabla
						DXCUtil.wait(1);
						optionNew = getItemSelected(LocListPaginacionPro);
					} while(optionOld == optionNew);
				} //else Reporter.writeTitle("NO se encontro link siguiente para intentar consultar paginación de procesos");
			}
			Evidence.saveFullPage("Buscando-Proceso", this);
		} while (objsiguiente != null);
		return "";
	}
//***********************************************************************************************************************
	/**
	 * Método que retorna el estado de la transacción consultada. Para ello se requiere el número del proceso y la fecha
	 * de este mismo <br>
	 * El retorno es [null] si no encuentra la transacción, en caso contrario retorna el estado respectivo.<br>
	 * La consulta se realiza para el mismo dia en fecha inicio y final, la data requerida es:<br>
	 * - fechaTrans: Dato tipo string que indica una fecha en el formato "09/07/2021" ("DD/MM/AAAA").<br>
	 * - numProceso: Dato tipo string que indica el número de proceso correspondiente a la transacción a consultar.
	 */
	public String getEstadoTransaccion(String fechaTrans, String numProceso) throws Exception {
		
		this.ingresarFechaInicial(fechaTrans);
		this.ingresarFechaFinal(fechaTrans);
		this.ingresarNumProceso(numProceso);
//		Evidence.save("Consulta-Estado", this);
		Evidence.save("Consulta-Estado");
		// Da clic en el botón buscar procesos
		this.clickBuscarProceso();
		// Valida el resultado de la búsqueda
		String msgError = this.resultadoBusqueda();
		if (!msgError.isEmpty()) {
//			Evidence.save("Error-Consulta-Estado", this);
			Evidence.save("Error-Consulta-Estado");
			return null;
		}
//-----------------------------------------------------------------------------------------------------------------------
		// SI LLEGA A ESTE PUNTO : NO SALIÓ ERROR EN LA CONSULTA, SE BUSCA LA TABLA DE RESULTADO
		String locColEstado1 = locColEstado.replace("PROCESO", numProceso).replace("FECHA", fechaTrans);
		WebElement objEstado = this.element(By.xpath(locColEstado1));
		if (objEstado == null) {
			Evidence.saveFullPage("Buscando-Estado", this);
			return null;
		}
		// EXISTE LA TABLA DE RESULTADO
		Evidence.saveFullPage("Estado-" + objEstado.getText(), this);
		WebElement objTablaDiv = this.element(locGrillaTabla);
		String estado = objEstado.getText();
		Evidence.saveFullElement("TablaEstado-" + estado, this, objTablaDiv, true, false);
		return estado;
	}
//***********************************************************************************************************************	
}
