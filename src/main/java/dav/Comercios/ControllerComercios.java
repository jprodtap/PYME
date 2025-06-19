package dav.Comercios;


import dav.FiduciaEstrusturada.PageFiduciaFront;
import dav.pymes.PageLoginPymes;
//import dxc.execution.Reporter;
import dxc.library.reporting.Reporter;


public class ControllerComercios {

/**
 * Page  y constructores	
 */
	PageComercios pageComercios = null;
	PageLoginPymes pageLoginPymes = null;
	public ControllerComercios(PageLoginPymes pageParent) {
		pageLoginPymes = pageParent;
	}
	
	int eventStatus = 0;
	
	public void consultasComercios() throws Exception{
	  
	String mensajeReporte = "";
	
	this.pageComercios = new PageComercios(this.pageLoginPymes); 
	boolean resultadoPrueba = this.pageComercios.validacionesComercios(); 
	if (resultadoPrueba) {
		eventStatus	= Reporter.MIC_PASS;
		mensajeReporte = this.pageComercios.getMensajes();
		Reporter.reportEvent(eventStatus, mensajeReporte);
		
	}else {
		eventStatus	= Reporter.MIC_FAIL;
		mensajeReporte = this.pageComercios.getMensajes();
		Reporter.reportEvent(eventStatus, mensajeReporte);
	}
	
	  
	}


	
	
	

}
