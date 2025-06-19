package dav.pymes.moduloTx;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dav.pymes.PageLoginPymes;
import dav.pymes.PagePortalPymes;
import dav.transversal.DatosEmpresarial;
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
	public PagePendAprobacion(PageLoginPymes parentPage) {
		super(parentPage);
	}
//***********************************************************************************************************************
	/**
	 * M�todo que ingresa al detalle de una transacci�n en las transacciones pendientes de aprobaci�n.<br>
	 * Retorna [null] si se encontr� la transacci�n y se ingres� a su detalle, en caso contrario retorna un mensaje.
	 */
	public String entrarDetalleTx(String numDocumento, boolean seEsperaMsgACH, String pageTitle) throws Exception {
		WebElement objLinkNumDoc = this.element(By.linkText(numDocumento));
		if (objLinkNumDoc == null) {
			Evidence.saveFullPage("NoExisteTx", this);
			return "Transacci�n [" + numDocumento + "] NO se encuentra en el listado de transacciones pendientes";
		}
		// SI LLEGA A ESTE PUNTO, LA TRANSACCI�N EXISTE : ENTRA A SU DETALLE
		this.click(objLinkNumDoc);
//-----------------------------------------------------------------------------------------------------------------------
		if (seEsperaMsgACH) { // DEBE MOSTRAR EL DI�LOGO DE ACH
			int timeEspera = 0;
			do {
				DXCUtil.wait(1);
				if (this.existDialog()) {
					DatosEmpresarial.ALERTA_ACH = this.getMessageDialog().contains("que realice a otros bancos");
					Evidence.saveWithDialog("AlertaACH", this, pageTitle);
					this.acceptDialog();
					break; // PARA QUE SALGA DEL CICLO
				}
				timeEspera++;
			} while (timeEspera <= 7); // M�XIMO DE ESPERA 7 SEGUNDOS
		}
//-----------------------------------------------------------------------------------------------------------------------
		this.waitIngresoModulo("Detalle de Transacci�n");
		Evidence.saveFullPage("DetalleTx", this);
		return null;
	}
//***********************************************************************************************************************
	/**
	 * M�todo que selecciona la transacci�n con documneto [numDocumento] en las transacciones pendientes de aprobaci�n.<br>
	 * Retorna [null] si se encontr� la transacci�n y se seleccion�, en caso contrario retorna un mensaje.
	 */
	public String seleccionarTransaccion(String numDocumento) throws Exception {
		WebElement objCheckBox = this.element(xPathCheckTx.replace("NUM_TX", numDocumento));
		if (objCheckBox == null) {
			Evidence.saveFullPage("NoExisteTx", this);
			return "Transacci�n [" + numDocumento + "] NO se encuentra en el listado de transacciones pendientes";
		}
		// SI LLEGA A ESTE PUNTO, LA TRANSACCI�N EXISTE
		this.click(objCheckBox);
//		Evidence.save("Selecci�nTx", this);
		Evidence.save("Selecci�nTx");
		return null;
	}
//***********************************************************************************************************************
	/**
	 * M�todo que ingresa el C�digo de Seguridad en la transacci�n que corresponde al documento [numDocumento].
	 */
	public void ingresarCodSeguridad(String numDocumento, String numCodSeg) throws Exception {
		WebElement objCvv = this.element(xPathCvv.replace("NUM_TX", numDocumento));
		if (objCvv != null) {
			this.write(objCvv, numCodSeg);
//			Evidence.save("IngresoCvv", this);
			Evidence.save("IngresoCvv");
		}
	}
//***********************************************************************************************************************
}
