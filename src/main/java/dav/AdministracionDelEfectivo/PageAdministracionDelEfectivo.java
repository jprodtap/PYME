package dav.AdministracionDelEfectivo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import dav.ActualizacionDeDatos.PageActualizacionDeDatos;

import dav.middlePymes.PageInicioMiddle;
import dav.AdministracionDelEfectivo.*;
import dav.pymes.PageLoginPymes;
import dav.pymes.moduloCrearTx.*;
import dav.transversal.DatosDavivienda;
import dav.transversal.DatosEmpresarial;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
import dxc.library.reporting.Reporter;
import dxc.library.settings.SettingsRun;
//import dxc.execution.Evidence;
//import dxc.execution.Reporter;
//import dxc.execution.SettingsRun;
import dxc.util.DXCUtil;

public class PageAdministracionDelEfectivo extends BasePageWeb {
	
	public PageAdministracionDelEfectivo(BasePageWeb parentPage) {
		super(parentPage);
	}
	
	PageConfirmacion pageConfirmacion = null;
	PageLoginPymes pageLogin = null;
 	PageConfiAproAdef pageAprobAdef = null;
	PageValores pageValores = null;
	ControllerDestinosMasivosAdef controlDestAdef = null;
	PageDestino pageDestino;
	PageMiddleConsultasAdef pageMiddleConsultasAdef;
	
	String sucursal;
	String descripcion;
	String tipoCarga;
	
	public static String numAprob;
	int posCtasDestino = 0;
	
	//Botones Iniciales
	By locBtnADEF = By.xpath("//*[@href=\"/administracion/paso1?tipo=N\"]");
	
	//Pendiente aprobacion
	By locBtnAprobarDisable = By.xpath("//input[@value=\"Aprobar Distribución\" and @disabled]");
	By locBtnGuardarSinApr = By.xpath("//input[@value=\"Guardar sin aprobar\"]");
	By locBtnAprobaciones = By.xpath("//*[@href=\"/aprobaciones\"]");
	String xpathCheckaprob = "/ancestor::tr/th/input";
	
	By locNumAproba = By.xpath("(//span[@class=\"num-doc\"])[2]");
	By locInfoSucursal = By.xpath("//div[@class=\"col-sm-4 col-md-3\"]/b[contains(text(), 'Sucursal')]");
	By locTableDestino = By.xpath("//div[@class=\"scroll-horizontal\"]");
	
	String xpathNumAprobPend;
	
	//Seleccionar sucursal
	By locBtnSiguiente = By.xpath("//button[@class=\"button--main\"][contains(text(), 'Siguiente')]");
	By locTablaSucursales = By.xpath("//*[@class=\"scroll-horizontal\"]");
	By locTodasSucursales = By.xpath("//td[contains(text(), 'Todas las Sucursales')]//following-sibling::td[1]");
	
	String xpathNumSucursal = "//*[@id=\"app\"]/div/div/div/div[2]/table/tr/td[3][contains(text(), 'NUM_SUCURSAL')]"; 
	String xpathNumSucursalClick;
	String xpathNumSucursalSaldo;
	
	//Adjuntar Archivo
	By locBuscarArchivo = By.xpath("//*[@id=\"app\"]/div/div/div[4]/div[1]/div[5]/input[3]");
	By locCargarArchivo = By.xpath("//*[@id=\"app\"]/div/div/div[4]/div[1]/div[6]/input");
	By locBtnVerificar = By.xpath("//input[@value=\"Verificar y Continuar\"]");
	
	By locRadioPrimer = By.xpath("(//input[@name=\"cuenta-distribucion\"])[1]");
	By locBtnAnterior = By.xpath("//input[@value=\"Anterior\"]");
	By locBtnGuardCamb = By.xpath("//input[@value=\"Guardar Cambios\"]");
	
	By locBtnModificar = By.xpath("//input[@value=\"Modificar\"]");
	By locBtnEliminar = By.xpath("//input[@value=\"Eliminar\"]");
	By locBtneliminarTod = By.xpath("//input[@value=\"Eliminar Todos\"]");
	
	By locNumCuentas = By.xpath("//td[@class=\"col__radio--width\"]");
	By locPagCuentas = By.xpath("//select[@class=\"pag__drop\"]");
	
	//Descripcion
	By locResumenDistri = By.xpath("//h2[contains(text(), 'Resumen de la distribución')]");
	By locDescripcion = By.xpath("//input[@type=\"text\"]");
	By locBtnSiguienteDes = By.xpath("//input[@value=\"Siguiente >\"]");
	By LocBtnContiErrores = By.xpath("//*[@value=\"Continuar con errores\"]");
	
	// LOCATOR PARA INGRESAR EL DESTINO PAGO DE ADEF
	//	By locBanDestNP = By.id("cphCuerpo_dropDestinoBanco");
	By locTipCtaDestNP = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::select)[1]");
	By locNumDestNP = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::input)[1]");
	By locTipIdNP = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::select)[2]");
	By locNumIdNP = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::input)[3]");
	By locValorPagoNP = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::input)[5]");
	By locReferNP = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::input)[6]");
	By locNomtitu = By.xpath("(//span[@style=\"display: none;\"]//preceding-sibling::input)[2]");
	
	//Aprobacion
	By locFechaApro = By.xpath("//div[@class=\"col-sm-3 col-md-2 text-sm-end mb-xs-1\"][4]");
	By locHoraApro = By.xpath("//div[@class=\"col-sm-3 col-md-2 text-sm-end mb-xs-1\"][5]");
	By locSaldoAprob = By.xpath("//*[@id=\"app\"]/div/div/div[5]/div[6]");
	By locCobroDistri = By.xpath("//*[@id=\"app\"]/div/div/div[6]/div[3]");
	By locMjsAlerta = By.xpath("//*[@id=\"app\"]/div/div/div[2]/div[2]/pre");
	
	//Consultas
	By locBtnConsultas = By.xpath("//*[@aria-current=\"page\"][contains(text(), 'Consultas')]");
	By locBtnConsDistribu = By.xpath("//*[@href=\"/consultas/distribuciones\"][contains(text(), 'Distribuciones')]");
	By locBtnConsregistro = By.xpath("//*[@href=\"/consultas/registros\"]");
	
	By locFechaDesde = By.xpath("//input[@placeholder=\"dd/mm/aaaa\"][1]");
	By locFechaHasta = By.xpath("//input[@placeholder=\"dd/mm/aaaa\"][2]");
	By locCmNumAprob = By.xpath("//div[@class=\"col-sm-8 col-md-4\"]/input");
	By locBtnBusDistri = By.xpath("//input[@value=\"Buscar distribuciones\"]");
	
	String xpathNumAprob = "//td/a[@href=\"#\"][contains(text(), 'NUM_APROB')]";
	String xpathEstado = "/parent::td/following-sibling::td[9]";
	
	By locDetalDistri = By.xpath("//*[@id=\"app\"]/div/div/div[3]");
	By locCntDestino = By.xpath("//table[@class=\"mt-1 mb-1\"]");
	By locPaginacion = By.xpath("//select[@class=\"pag__drop\"]");
	
	//Metodo inicial para ingresar y seleccionar en la transaccion de ADEF
	public void InicioAdministracionDelEfectivo() throws Exception {
		
		this.sucursal = SettingsRun.getTestData().getParameter("Sucursal").trim();
		this.descripcion = SettingsRun.getTestData().getParameter("Descripcion").trim();
		this.tipoCarga = SettingsRun.getTestData().getParameter("Tipo de Carga").trim();
		
		//Selecciona el Iframe de ADEf
		this.getDriver().switchTo().frame("cphCuerpo_IframeAdef");
		
        Date horaActual = new Date();

        SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm:ss a");
        String horaFormateada = formatoHora.format(horaActual);

        pageLogin = new PageLoginPymes(this);
        pageMiddleConsultasAdef = new PageMiddleConsultasAdef(pageLogin);
        pageMiddleConsultasAdef.horaInicio = horaFormateada;
        
		do {
		} while (!this.isDisplayed(locBtnADEF));
		
		this.click(locBtnADEF);
		
		do {
		} while (!this.isDisplayed(locTablaSucursales));
		DXCUtil.wait(10);
		
		Evidence.save("Sucursales");
		xpathNumSucursal = xpathNumSucursal.replace("NUM_SUCURSAL", sucursal);
		
		if (this.isDisplayed(this.element(xpathNumSucursal))) {
			
			//Se obtiene el Xpath de checkbox para seleccionar la sucursal
			xpathNumSucursalClick = xpathNumSucursal + "//preceding-sibling::td[2]/input";
			
//			//Se obtiene el Xpath para extraer el valor actual de la sucursal
//			xpathNumSucursalSaldo = xpathNumSucursal + "//preceding-sibling::td[2]";
//			
//			String saldo = this.element(xpathNumSucursalSaldo).getText();
//			
//			Reporter.write(saldo);
			
			//Selecciona el checkbox de la sucursal a utilizar
			this.click(this.element(xpathNumSucursalClick));
			
//			String[] msg = DatosDavivienda.STRATUS.consultarDatosPantallaSaldos("Nit", "8300035425", "CC", "0560099869989923");
			
			Evidence.save("Sucursal_Seleccionada");
		
			//Valida que el codigo 9999 sea el que representa todas las sucursales
			if (this.isDisplayed(locTodasSucursales)) {
				
				if (this.element(locTodasSucursales).getText().equals("9999")) {
					Reporter.reportEvent(Reporter.MIC_PASS, "sucursales_correcto codigo de todas las sucrursales");
				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "sucursales_incorrecto codigo de todas las sucrursales");
				}
				
			}
			
			this.click(locBtnSiguiente);
			DXCUtil.wait(5);
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "No se encuentra la sucursal o esta restringida");
			this.pageLogin.CerrarSesionFront();
			SettingsRun.exitTestIteration();
		}

		controlDestAdef = new ControllerDestinosMasivosAdef(this);
		
		//Realiza el cargue de las cuentas destino con cualquiera de los 3 metodos
		if (tipoCarga.equals("DIGITADO")) 
			this.CargarDestinos();
		
		if (tipoCarga.equals("EXCEL"))
			this.controlDestAdef.cargarDestinos();
		
		if (tipoCarga.equals("PLANO"))
			this.controlDestAdef.cargarDestinos();
	}
	
	//Se utiliza para cargar las cuentas destino de la forma digitado
	public void CargarDestinos() throws Exception {
		
		Evidence.save("Pagina para cargar destinos");
		
//		controlDestAdef = new ControllerDestinosMasivosAdef(this);
		String msgError = controlDestAdef.cargarDestinos();
		
		Evidence.save("Destinos digitados");
		
	}
	
	//Adjunta el archivo de las cuentas destino para el motodo EXCEL o Archivo PLANO
	public void CargarArchivo() throws Exception {
		
		Evidence.save("Pagina para cargar destinos");
		
		do {
		} while (!this.isDisplayed(locBuscarArchivo));
		this.click(locBuscarArchivo);
		
		DXCUtil.wait(5);
		
		this.pageValores = new PageValores(this);
		this.pageValores.adjuntarArchivoOrpa();
		
		Evidence.save("archivo adjunto");
		
		do {
		} while (!this.isDisplayed(locCargarArchivo));
		this.click(locCargarArchivo);
		
		do {
		} while (!this.isDisplayed(locCargarArchivo));
		
		Evidence.save("Destinos cargados");

	}

	//Adiciona las cuentas destino solo para el metodo DIGITADO
	public String adicionarDestino(String tipoId, String numId, String nombre, String apellido, String bancoDest,
			String tipoCtades, String numProdes, String valorPago, String referencia) throws Exception {
		
		String nombre_Titular = nombre + "" + apellido;
//		this.pageOrigen.waitPantallaDestino();
		DXCUtil.wait(5);
	
//		System.out.println( tipoId+"-"+  numId+"-"+  nombre+"-"+  apellido+"-"+  bancoDest+"-"+
//			 tipoCtades+"-"+  numProdes+"-"+  valorPago+"-"+  referencia);
//		
		if (tipoId.contains("CIUDADANÍA")) {
			tipoId = "Cédula de Ciudadanía";
		}
		
		String msgError = this.selectListItem(locTipIdNP, tipoId);
		
		if (msgError.isEmpty())
			
			return "Error en lista 'Tipo de Identificación' : " + msgError;
		
		this.write(locNumIdNP, numId);

		this.write(locNomtitu, nombre_Titular.toUpperCase());
		
		this.selectListItem(locTipCtaDestNP, tipoCtades);

		this.write(locValorPagoNP, valorPago);

		this.write(locReferNP, referencia);

		this.write(locNumDestNP, numProdes);

		if (tipoCtades.contains("DEPÓSITOS ELECTRÓNICOS")) {
			tipoCtades = "DEPÓSITOS ELECTRONICOS";
		}

		this.selectListItem(locTipCtaDestNP, tipoCtades);
		
		this.clickButton("Adicionar Cuenta");
		
		Evidence.save("destino adicionado");
		
//		msgError = this.seleOptionIgual(locTipCtaDestNP, tipoCtades);
//		if (msgError.isEmpty())
//			return "Error en lista 'Tipo Destino' : " + msgError;
		
		return null;
	}
	
	//Realiza la aprobacion de ADEF para Aprobacion en linea o Pendiente Aprobacion
	public String[] AprobacionTxAdef() throws Exception {
		
		int intNumCuentas;
		String msjRespuesta = null;
		String MsgAlerta = null;
		
		String descripcion = SettingsRun.getTestData().getParameter("Descripcion").trim();
		String tipoPrueba = SettingsRun.getTestData().getParameter("Tipo prueba").trim();
		String riesgoBC = SettingsRun.getTestData().getParameter("Nivel de Riesgo BC").trim();
		String riesgoSASEFM = SettingsRun.getTestData().getParameter("Nivel de Riesgo SAS EFM").trim();
		
		boolean primeroGuardar = tipoPrueba.equals("Tx Pend Aprobación");
		
		// NO SALVA EVIDENCIA PORQUE COMO ES PAGO MASIVO, SE SALVA LA EVIDENCIA CUANDO
		// VAYA A SEGUIR CON EL PAGO
		Evidence.save("ProdDestinos");
		DXCUtil.wait(4);
		
		if (this.isDisplayed(locBtnModificar) && this.isDisplayed(locBtnEliminar) && this.isDisplayed(locBtneliminarTod)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "aprobarTX_se encuentra botonera de modificar y eliminar cuentas");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "aprobarTX_no encuentra botonera de modificar y eliminar cuentas");
		}
		
		List<WebElement> numCuentas = this.findElements(locNumCuentas);
		intNumCuentas = numCuentas.size();
		
		if (intNumCuentas == 20) {
			
			if (this.isDisplayed(locPagCuentas)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "aprobarTX_se encuentra paginacion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "aprobarTX_no se encuentra paginacion");
			}
		}
		
		if (!this.isDisplayed(locBtnAnterior)) {
			Reporter.reportEvent(Reporter.MIC_FAIL, "aprobarTX_no se encuentra boton anterior");
		}
		
		this.click(locRadioPrimer);
		DXCUtil.wait(1);
		this.click(locBtnModificar);
		
		do {
		} while (this.element(locBtnGuardCamb) == null);
		
		if (this.isDisplayed(locInfoSucursal)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "aprobarTX modificar_permite modificar destinos");
			Evidence.save("aprobarTX_ se puede modificar destinos");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "aprobarTX_no permite modificar destinos");
		}
		
		this.click(locBtnAnterior);
		
		do {
		} while (this.element(locBtnModificar) == null);
		
		this.clickButton("Verificar y Continuar");
		
		DXCUtil.wait(10);

		Evidence.save("cargar destino_ siguiente");
		
		if (this.element(LocBtnContiErrores) != null) {
			this.click(LocBtnContiErrores);
		}
		
//		do {
//		} while (!this.isDisplayed(locBtnSiguienteDes));
		
		do {
		} while (!this.isDisplayed(locDescripcion));
		
		Evidence.save("descripcion");
		
		DXCUtil.wait(10);
		
		this.write(locDescripcion, descripcion);
		this.click(locBtnSiguienteDes);	
		
		if (this.isDisplayed(locResumenDistri)) {
			Reporter.reportEvent(Reporter.MIC_PASS, "confirmacion_se encuentra resumen de distribucion");
		} else {
			Reporter.reportEvent(Reporter.MIC_PASS, "confirmacion_no se encuentra el resumen de distribucion");
		}
		
		Evidence.save("Pagina para confirmacion_descripcion");
		
		this.pageAprobAdef = new PageConfiAproAdef(this);
		
		if (primeroGuardar) {
			
			do {
			} while (!this.isDisplayed(locBtnGuardarSinApr));
			this.click(locBtnGuardarSinApr);
			
			Evidence.save("Pendiente aprobar");
			
			MsgAlerta = this.MsgAlerta();
			
			if (!MsgAlerta.equals(null)) 
			Reporter.reportEvent(Reporter.MIC_PASS, MsgAlerta);
			
			WebElement numAprobacion = this.element(locNumAproba);
			String numApro = numAprobacion.getText();
			
			Evidence.save("Numero de aprobacion");
			
			this.numAprob = numApro;
			
			this.PendienteAprobar();
			msjRespuesta = this.pageAprobAdef.aprobarTxADEF(primeroGuardar);
			
			if (this.element(xpathNumAprobPend) != null) {
				Reporter.reportEvent(Reporter.MIC_PASS, "aprobacion_no se encuentra el registro en aprobaciones");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "aprobacion_se encuentra el registro en aprobaciones");
			}
			
		} else {
			
			do {
			} while (!this.isDisplayed(locBtnGuardarSinApr));

			if (this.isDisplayed(locBtnAprobarDisable)) {			
				Reporter.reportEvent(Reporter.MIC_FAIL, "No tiene permisos para aprobar distribuciones");
				this.pageLogin.CerrarSesionFront();
				SettingsRun.exitTestIteration();
			}

			msjRespuesta = this.pageAprobAdef.aprobarTxADEF(primeroGuardar);
		}
		
		Evidence.save("Aprobacion");
		pageMiddleConsultasAdef.numAprob = numAprob;
		
		if (msjRespuesta.equals("La distribución ha sido aprobada por los usuarios autorizados. A continuación se muestran los datos de la distribución y el número de documento asignado por el Banco. "
				+ "Puede conocer el estado final de la distribución en el módulo de consultas.")) {
			
			
			if (riesgoBC.equals("Bajo")||riesgoBC.equals("Medio")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "riesgoBC_correcto funcionamiento en riesgo Biocatch");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "riesgoBC_incorrecto funcionamiento en riesgo Biocatch");
			}
			
			if (riesgoSASEFM.equals("Bajo")||riesgoSASEFM.equals("Medio") ) {
				Reporter.reportEvent(Reporter.MIC_PASS, "riesgoSASFM_correcto funcionamiento en riesgo SAS FM");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "riesgoSASEFM_incorrecto funcionamiento en riesgo SAS FM");
			}
		
		//Revisar riesgo	
		} else if (msjRespuesta.equals("Error por riego alto")) {
			
			if (riesgoBC.equals("Alto")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "riesgoBC_correcto funcionamiento en riesgo Biocatch");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "riesgoBC_incorrecto funcionamiento en riesgo Biocatch");
			}
			
			if (riesgoSASEFM.equals("Alto")) {
				Reporter.reportEvent(Reporter.MIC_PASS, "riesgoSASFM_correcto funcionamiento en riesgo SAS FM");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "riesgoSASEFM_incorrecto funcionamiento en riesgo SAS FM");
			}	
			
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, msjRespuesta);
			this.pageLogin.CerrarSesionFront();
			SettingsRun.exitTestIteration();
		}
		
		String fechaAprob = this.element(locFechaApro).getText();
		String horaAprob = this.element(locHoraApro).getText();
		String saldoAprob = this.element(locSaldoAprob).getText();
		String saldoCobroDis = this.element(locCobroDistri).getText();
		
		Reporter.reportEvent(Reporter.MIC_PASS, msjRespuesta);
		String data[] = {fechaAprob + " " + horaAprob, saldoAprob, horaAprob , saldoCobroDis};
		
		return data;
		
	}
	
	public String ConsultaDistribucion() throws Exception {
		
		Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = formatoFecha.format(fechaActual);
		
		String msgAlerta;
		String msgError = "Elemento no encontrado";
		String xpathNumAprobLinea = null;
		String columEstado = null;  
		int NumEstados;
		
		do {
		} while (this.element(locBtnConsultas) == null);

		this.click(locBtnConsultas);	
		this.click(locBtnConsDistribu );	
		
		do {
		} while (this.element(By.xpath("//div/select/option")) == null);
		
		DXCUtil.wait(1);
		
		List<WebElement> Estado = this.findElements(By.xpath("//div/select/option"));
		NumEstados = Estado.size();
		
		if (NumEstados == 9) {
			Reporter.reportEvent(Reporter.MIC_PASS, "distribucion_estados correctos");
		} else {
			Reporter.reportEvent(Reporter.MIC_FAIL, "distribucion_estados incorrectos");
		}
		
		do {
		} while (this.element(locFechaDesde) == null);
		
		Evidence.save("filtros de busqueda_ front distri");
		
		this.write(locFechaDesde, fecha);
		this.write(locFechaHasta, fecha);
		this.write(locCmNumAprob, numAprob);
		this.click(locBtnBusDistri);	
		
		Evidence.save("filtros de busqueda ok_ front distri");
		
		xpathNumAprobLinea = xpathNumAprob.replace("NUM_APROB", numAprob);
		
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
		} while (this.element(xpathNumAprobLinea) == null && contador<10);
		
		if (contador == 10) {
			Evidence.save("error no se encontro registro_ front distri");
			Reporter.reportEvent(Reporter.MIC_FAIL, "no se encontro el registro con numero de aprobacion: "+ numAprob);
			return msgError;
		} else {
			
			columEstado = xpathNumAprobLinea + xpathEstado;
			Reporter.reportEvent(Reporter.MIC_PASS, "Estado del registro: "+ this.element(columEstado).getText());
			
			this.click(this.element(xpathNumAprobLinea));
			
			msgAlerta = this.MsgAlerta();
			if (msgAlerta != null) {
				Evidence.save("mensaje de alerta");
				Reporter.reportEvent(Reporter.MIC_FAIL, "Se encontro alerta en el portal: " + msgAlerta);
//				return msgAlerta;
			}
			
			if ((this.element(locCntDestino) != null && (this.element(locCntDestino) != null))) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se encontro en Cons_distribucion, cuentas destino y paginacion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro en Cons_distribucion, cuentas destino y paginacion");
			}
			
			if (this.element(locDetalDistri) != null) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Se encontro en Cons_distribucion, detalle distribucion");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "No se encontro en Cons_distribucion, detalle distribucion");
			}
			
			Evidence.save("detalles de busqueda_ front distri");
			
		}
			
		return null;
		
	}
	
	//Si encuentra no encuentra mensaje de alerta retorna null de lo contrario retorna el mensaje de alerta
	public String PendienteAprobar() throws Exception {
		
		int intNumCuentas = 0;
		String msgError = "Elemento no encontrado";
		String checkAprob = null;  
		
		this.click(locBtnAprobaciones);
		
		xpathNumAprobPend = xpathNumAprob.replace("NUM_APROB", numAprob);
		
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
		} while (this.element(xpathNumAprobPend) == null && contador<10);
		
		Evidence.save("Pagina de aprobaciones");
		
		if (contador == 10) {
			Evidence.save("Error en pendiente aprobacion");
			Reporter.reportEvent(Reporter.MIC_FAIL, "no se encontro el registro con numero de aprobacion: "+ numAprob);
			return msgError;
			
		} else {
			
			this.click(this.element(xpathNumAprobPend));
			
			do {
			} while (this.element(locTableDestino) == null);
			
			Evidence.save("Pendiente aprobar detalle");

			DXCUtil.wait(2);
			
			if ((this.element(locTableDestino) != null && this.element(locInfoSucursal) != null)) {
				Reporter.reportEvent(Reporter.MIC_PASS, "Pendiente_aprobacion detalles, se encontro tabla de destinos y No. de sucursal");
			} else {
				Reporter.reportEvent(Reporter.MIC_FAIL, "Pendiente_aprobacion detalles, no se encontro tabla de destinos y No. de sucursal");
			}
			
			List<WebElement> numCuentas = this.findElements(locNumCuentas);
			intNumCuentas = numCuentas.size();
			
			if (intNumCuentas == 20) {
				
				if (this.isDisplayed(locPagCuentas)) {
					Reporter.reportEvent(Reporter.MIC_PASS, "aprobarTX_se encuentra paginacion");
				} else {
					Reporter.reportEvent(Reporter.MIC_FAIL, "aprobarTX_no se encuentra paginacion");
				}
			}

			this.click(locBtnAprobaciones);
	
			do {
			} while (this.element(xpathNumAprobPend) == null);

			checkAprob = xpathNumAprobPend + xpathCheckaprob;
			this.click(this.element(checkAprob));
			Evidence.save("Pendiente aprobar seleccionado");
		}
		
		return null;
	}
	
	//Si encuentra no encuentra mensaje de alerta retorna null de lo contrario retorna el mensaje de alerta
	public String MsgAlerta() throws Exception {
		
		String msgAlerta;
		
		int contador = 0;
		do {
			contador++;
			DXCUtil.wait(1);
		} while (this.element(locMjsAlerta) == null && contador<5);
		
		if (contador != 5) {
			WebElement msgAdef = this.element(locMjsAlerta);
			msgAlerta = msgAdef.getText();
			Evidence.save("mensaje alerta");
			return msgAlerta;
			
		} else {
			return null;
		}
		
	}

}
