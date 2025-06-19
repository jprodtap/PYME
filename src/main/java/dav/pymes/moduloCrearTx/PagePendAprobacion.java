package dav.pymes.moduloCrearTx;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PagePortalPymes;
import dxc.execution.BasePageWeb;
import dxc.library.reporting.Evidence;
//import dxc.execution.Evidence;
import dxc.util.DXCUtil;

public class PagePendAprobacion extends PagePortalPymes {
	
	String xPathCheckTx = "//a[contains(text(),'NUM_TX')]//parent::td//preceding-sibling::td/input[@type='checkbox']";
	String xPathCvv     = "//a[contains(text(),'NUM_TX')]//parent::td//following-sibling::td/input";
	
//=======================================================================================================================
	/**
	 * Constructor a partir del [BasePageWeb] padre.
	 */
	public PagePendAprobacion(BasePageWeb parentPage) {
		super(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * Metodo entrarDetalleTx BUSCA LA TRANSACCIÓN EXISTE : ENTRA A SU DETALLE 
	 * @param numDocumento
	 * @return
	 * @throws Exception
	 */
	public String entrarDetalleTx(String numDocumento) throws Exception {
		WebElement objLinkNumDoc = this.element(By.linkText(numDocumento));
		if (objLinkNumDoc == null) {
			Evidence.saveFullPage("NoExisteTx", this);
			return "Transacción [" + numDocumento + "] NO se encuentra en el listado de transacciones pendientes";
		}
		// SI LLEGA A ESTE PUNTO, LA TRANSACCIÓN EXISTE : ENTRA A SU DETALLE
		this.click(objLinkNumDoc);
		DXCUtil.wait(10);
		this.existDialogAccept();
		DXCUtil.wait(1);
		Evidence.saveFullPage("DetalleTx", this);
		return null;
	}
//***********************************************************************************************************************
	public String seleccionarTransaccion(String numDocumento) throws Exception {
		WebElement objCheckBox = this.element(xPathCheckTx.replace("NUM_TX", numDocumento));
		if (objCheckBox == null) {
			Evidence.saveFullPage("NoExisteTx", this);
			return "Transacción [" + numDocumento + "] NO se encuentra en el listado de transacciones pendientes";
		}
		// SI LLEGA A ESTE PUNTO, LA TRANSACCIÓN EXISTE
		this.click(objCheckBox);
		Evidence.saveFullPage("SelecciónTx", this);
		return null;
	}
//***********************************************************************************************************************
	public void ingresarCodSeguridad(String numCodSeg) throws Exception {
		WebElement objCvv = this.element(xPathCvv);
		if (objCvv != null)
			this.write(objCvv, numCodSeg);
		Evidence.saveFullPage("IngresoCvv", this);
	}
//***********************************************************************************************************************
	
	
	
	
}
