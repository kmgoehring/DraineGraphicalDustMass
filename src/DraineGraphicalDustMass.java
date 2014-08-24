import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 * 
 * @author Kevin M. Goehring
 * A brief class/UI that walks a user through implementation of the Draine/Li dust model
 * as outlined in the paper "Draine, B. T., et al. 2007, ApJ, 663, 866"
 * for estimating a galaxy's dust mass via a collection of infrared flux measurements. 
 * Handles the repetitive mathematical calculations that would result from running the model 
 * for multiple objects and reminds the user how to use the graphs contained in the aforementioned 
 * paper. 
 */

public class DraineGraphicalDustMass {
	
	public static void main(String[] args) {
		
		/**
		 * String variables that represent user data entered into the console. 
		 */
		String DISTANCE, 
		firstFlux, 
        secondFlux, 
        thirdFlux, 
        fourthFlux, 
        fifthFlux,  
        UMIN, 
        GAMMA,  
        PSI; 
    
		/**
		 * Double variables (some converted from the user entered strings) for running
		 * the mathematical calculations. 
		 */
	double distance,  // Objects distance, in megaparsecs. 
		   flux3_6,  // 3.6 micron flux, in janskys
		   flux7_9,  // 7.9 micron flux, in janskys
		   flux24,  // 24 micron flux, in janskys
		   flux71,  // 71 micron flux, in janskys
		   flux160,  // 160 micron flux, in janskys
		   NSflux7_9, // 7.9 micron "Non-Stellar" flux, in janskys
		   NSflux24,  // 24 micron "Non-Stellar" flux, in janskys
		   P7_9,  // Model specific parameter. Ratio of some fluxes and proportional to PAH radiation. 
		   P24,  // Model specific parameter.  Ratio of some fluxes and sensitive to the gamma paramter. 
		   R71,  // Model specific parameter. Ratio of some fluxes and sensitive to far-infrared emissions. 
		   Umin,  // Minimum starlight intensity. 
		   gamma,  //Fraction of dust mass exposed to starlight. 
		   U,  // Mean starlight intenstiy, <U>
		   psi, // Model speficific parameter used to calculate the final dust mass. 
		   Mdust;  // Dust mass, in solar masses. 
	
	
	/**
	 * The following are a series of JOptionPane prompts for the user to enter their data. 
	 */
		DISTANCE = JOptionPane
				.showInputDialog("Enter the distance to your object in megaparsecs");

		firstFlux = JOptionPane
				.showInputDialog("Enter 3.6 micron flux (in janskys) for your object");

		secondFlux = JOptionPane
				.showInputDialog("Enter 7.9 micron flux (in janskys) for your object");

		thirdFlux = JOptionPane
				.showInputDialog("Enter 24 micron flux (in janskys) for your object");

		fourthFlux = JOptionPane
				.showInputDialog("Enter 71 micron flux (in janskys) for your object");

		fifthFlux = JOptionPane
				.showInputDialog("Enter 160 micron flux (in janskys) for your object");
				
	// Convert numbers from type String to type double for use in calculations. 

		distance = Double.parseDouble (DISTANCE);
		flux3_6 = Double.parseDouble ( firstFlux); 
		flux7_9 = Double.parseDouble ( secondFlux); 
		flux24 = Double.parseDouble ( thirdFlux);
		flux71 = Double.parseDouble ( fourthFlux);
		flux160 = Double.parseDouble (fifthFlux);
		
	// Establish two useful decimal formats. 
		DecimalFormat df = new DecimalFormat("0.##E0");
		DecimalFormat DF = new DecimalFormat("0.00#");
	
/**
 * Calculates the non-stellar fluxes as well as P(7.9), P(24), and R(71). Display values to user. 
 * Instructs user how to use Figure 20 to find q(PAH) and Figure 21 to find U(MIN) and GAMMA.
 * Figures located in the referenced paper. 
 * Asks the user to input U(MIN) and GAMMA. 
 */
		
NSflux7_9 = flux7_9 - (0.232 * flux3_6);
NSflux24 = flux24 - (0.032 * flux3_6);
P7_9 = (3.7948E13 * NSflux7_9) / (4.222E12 * flux71 + 1.8737E12 * flux160);
P24 = (1.2491E13 * NSflux24) / (4.222E12 * flux71 + 1.8737E12 * flux160);
R71 = (4.222E12 * flux71) / (1.8737E12 * flux160);


UMIN = JOptionPane.showInputDialog ( 
null, "The P(7.9) value is " + DF.format(P7_9)+ "." 
+ "\n"
+ " \nThe P(24) value is " + DF.format(P24)+ "." 
+ "\n"
+ " \nThe R(71) values is " +DF.format(R71) + "."
+ "\n"
+ " \nUse R(71) and and P(7.9) to estimate q(PAH) by finding" +
"\nthe value of q(PAH) such that the point (R71, P7.9) falls just above the GAMMA = 0 " +
"curve in Figure 20 "+"."
+ "\n"
+ " \nOnce you have your q(PAH) value, use it to locate (R71, P24) on Figure 21 to determine U(MIN) and GAMMA. "
+ "\n"
+ " \nPlease enter the value you found for U(MIN): ", 
"Results", 
JOptionPane.PLAIN_MESSAGE); 



/**
 * Prompt the user to enter their GAMMA value as a string
 */
GAMMA = 
JOptionPane.showInputDialog ( "Please enter the value you found for GAMMA" );

/**
 * Instructs user how to find PSI and asks them to input it
 */

PSI = JOptionPane.showInputDialog (
null, "Using the values of q(PAH), U(MIN), and GAMMA, use Figure 23 to find PSI." +
		"\nPlease enter the value you found for PSI: ",
		"Reults",
		JOptionPane.PLAIN_MESSAGE);

//Convert U(MIN), GAMMA and PSI from string to double
Umin = Double.parseDouble (UMIN); 
gamma = Double.parseDouble (GAMMA);
psi = Double.parseDouble (PSI);

/**
 *  Calculate <U>, the mean starlight intensity. 
 *  The x, y, and z variables are for simplification of the equations. 
 */

		double x = (1 - gamma) * Umin, y = gamma * Umin
				* (Math.log(1000000 / Umin)), z = 1 - (Umin / 1000000);

		U = x + (y / z);

/**
 * Calculate final Dust Mass Estimate and display it to the user!
 */

		Mdust = (psi / U)
				* ((1.2491E13 * flux24) + (4.222E12 * flux71) + (1.8737E12 * flux160))
				* (distance * distance) * 9.521E22 / 1.9891E30;

		JOptionPane.showMessageDialog(null,
				"The dust mass estimate for your object is " + df.format(Mdust)
						+ " solar masses.", "Results",
				JOptionPane.PLAIN_MESSAGE); 

		System.exit(0); // ends the program
		   
	}
}

