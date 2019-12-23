package ca.fourthreethreefour.wrapper;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

public class CANEncoderWrapper extends CANEncoder {

    private double m_start;

    public CANEncoderWrapper(CANSparkMax device) {
        super(device);
    }

    public void reset() {
        m_start = super.getPosition();
    }
    
    public double get() {
        return super.getPosition() - m_start;
    }

}