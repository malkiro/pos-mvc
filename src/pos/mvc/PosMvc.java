/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.mvc;

import pos.mvc.view.CustomerView;

/**
 *
 * @author PC
 */
public class PosMvc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CustomerView().setVisible(true);
    }
    
}
