package br.com.irisbot.jarvis.raspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GpioUtils {

	private static final GpioController gpio = GpioFactory.getInstance();
    private static final GpioPinDigitalOutput Pin00 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Pin00", PinState.LOW);
    private static final GpioPinDigitalOutput Pin01 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Pin01", PinState.LOW);
    private static final GpioPinDigitalOutput Pin02 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Pin02", PinState.LOW);
    private static final GpioPinDigitalOutput Pin03 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Pin03", PinState.LOW);
    private static final GpioPinDigitalOutput Pin04 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Pin04", PinState.LOW);
    private static final GpioPinDigitalOutput Pin05 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin05", PinState.LOW);
    private static final GpioPinDigitalOutput Pin06 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Pin06", PinState.LOW);

    private static final GpioPinDigitalOutput[] pins = {Pin00,Pin01,Pin02,Pin03,Pin04,Pin05,Pin06}; 
    private static final boolean[] states = {false,false,false,false,false,false,false}; 
    
    public static boolean isHigh(int pin){
    	return states[pin];//PinState.HIGH.equals(pins[pin].getState());
    }
    
    public static void high(final int pin, boolean async){
    	Thread t = new Thread() {
			@Override
			public void run() {
				states[pin] = true;
		    	pins[pin].high();
			}
		};
		if(async) t.start(); else t.run();
    }

    public static void low(final int pin, boolean async){
    	Thread t = new Thread() {
			@Override
			public void run() {
				states[pin] = false;
		    	pins[pin].low();
			}
		};
		if(async) t.start(); else t.run();
    }
    
    public static void pulse(final int pin, final long mils, boolean async){
    	Thread t = new Thread() {
			@Override
			public void run() {
				if(!states[pin]){
					states[pin] = true;
			    	pins[pin].high();
			    	try{sleep(mils);}catch(Exception e){}
			    	states[pin] = false;
			        pins[pin].low();
				}
			}
		};
		if(async) t.start(); else t.run();
    }
    

	
}
