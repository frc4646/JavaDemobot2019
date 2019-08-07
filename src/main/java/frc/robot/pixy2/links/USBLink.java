package frc.robot.pixy2.links;
import org.usb4java.LibUsb;
import org.usb4java.Context;
import org.usb4java.LibUsbException;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.time.Duration;
import java.time.Instant;
import frc.robot.pixy2.Pixy2.Checksum;


public class USBLink  implements Link
{
    // Them flags
    protected final int LINK_FLAG_SHARED_MEM     =                       0x01;
    protected final int	LINK_FLAG_ERROR_CORRECTED           =            0x02;

    // result codes
    //private final int LINK_RESULT_OK                    =              0;
    //private final int LINK_RESULT_ERROR                  =             -100;
    protected final int LINK_RESULT_ERROR_RECV_TIMEOUT      =            -101;
    protected final int LINK_RESULT_ERROR_SEND_TIMEOUT       =           -102;

    // link flag index
    protected final byte LINK_FLAG_INDEX_FLAGS                     =      0x00;
    protected final byte LINK_FLAG_INDEX_SHARED_MEMORY_LOCATION     =     0x01;
    protected final byte LINK_FLAG_INDEX_SHARED_MEMORY_SIZE         =     0x02;

    public static final short PIXY_VID    = (short)  0xB1AC;
    public static final short PIXY_PID    = (short)  0xF000;


    Context m_context;
    DeviceHandle m_handle;
    Instant m_timer;
    protected int m_flags = 0;
    protected int m_blockSize = 0;

    public USBLink() {
        m_handle = null;
        m_context = null;
        m_blockSize = 64;
        m_flags = LINK_FLAG_ERROR_CORRECTED;    
    }

    public int open(int arg) {
        close();

        m_context = new Context();
        LibUsb.init(m_context);
    
        return openDevice();
    
    }

    public void close() {
        
        if (m_handle!=null) {
            LibUsb.close(m_handle);
            m_handle=null;
        }
        if (m_context!=null)
        {
            LibUsb.exit(m_context);
            m_context=null;
        }

    }


    public int send(byte[] data, int timeoutMs) {
        
        if (timeoutMs==0) // 0 equals infinity
            timeoutMs = 10;

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        buffer.put(data);
        IntBuffer transferred = IntBuffer.allocate(1);
        int result = LibUsb.bulkTransfer(m_handle, (byte)0x02, buffer, transferred, timeoutMs); 
        if (result != LibUsb.SUCCESS) throw new LibUsbException("Control transfer failed", transferred.get());
        int sent = transferred.get();
        System.out.println(sent + " bytes sent");
        return sent;
    }

    public int receive(byte[] data, int timeoutMs, Checksum cs) {
        
        if (timeoutMs==0) // 0 equals infinity
            timeoutMs = 100;
    
        // Note: if this call is taking more time than than expected, check to see if we're connected as USB 2.0.  Bad USB cables can
        // cause us to revert to a 1.0 connection.
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
        IntBuffer transferred = IntBuffer.allocate(1);
        int result=LibUsb.bulkTransfer(m_handle, (byte)0x82, buffer, transferred, timeoutMs);
        if (result != LibUsb.SUCCESS) throw new LibUsbException("Control transfer failed", transferred.get());
        int received = transferred.get();
        buffer.get(data,0, received);
        return received;
    }

    public int receive(byte[] data, int timeoutMs) {
        return receive(data, timeoutMs, null);
    }
     
    public void setTimer() 
    {
          m_timer = Instant.now();
    }

    public int getTimer()
    {
        long time = Duration.between(m_timer,Instant.now()).toMillis();
        return (int)time;
    }

    private int openDevice() {
        DeviceList list = null;
        int result = 0;
        int returnValue = -1;
        
        DeviceDescriptor desc = null;

        list = new DeviceList();
        result = LibUsb.getDeviceList(m_context, list);
        if (result < 0) throw new LibUsbException("Unable to get device list", result);

        for (Device device: list)
        {
            DeviceDescriptor descriptor = new DeviceDescriptor();
            result = LibUsb.getDeviceDescriptor(device, descriptor);
            if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
            if (desc.idVendor()==PIXY_VID && desc.idProduct()==PIXY_PID)
            {
                m_handle=new DeviceHandle();
                result = LibUsb.open(device, m_handle);
                if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to open device", result);
                // was if MACOS
                //LibUsb.resetDevice(m_handle);
                //
                if (LibUsb.setConfiguration(m_handle, 1)<0)
                {
                        LibUsb.close(m_handle);
                        m_handle = null;
                        continue;
                }
                if (LibUsb.claimInterface(m_handle, 1)<0)
                    {
                        LibUsb.close(m_handle);
                        m_handle = null;
                        continue;
                    }
                // was if LINUX
                LibUsb.resetDevice(m_handle);
                //
                returnValue=0;
                break;
            
            }
            
        }

        LibUsb.freeDeviceList(list, true);
        return returnValue;
    }
    //TODO: Finish and test the USBLink.
}