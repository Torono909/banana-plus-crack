// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils.Verification;

import java.util.Properties;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;

public final class HWIDUtils
{
    public static String encode(final String key) {
        final String sha512 = Hashing.sha512().hashString((CharSequence)key, StandardCharsets.UTF_8).toString();
        return Hashing.sha256().hashString((CharSequence)sha512, StandardCharsets.UTF_8).toString();
    }
    
    private static String getCpuId() {
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            final Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            return sc.next();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "ERROR";
        }
    }
    
    private static String getBiosVersion() {
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "bios", "get", "ReleaseDate" });
            process.getOutputStream().close();
            final Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            return sc.next();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "ERROR";
        }
    }
    
    private static String getBaseboardManufacturer() {
        try {
            final Process process = Runtime.getRuntime().exec(new String[] { "wmic", "baseboard", "get", "Manufacturer" });
            process.getOutputStream().close();
            final Scanner sc = new Scanner(process.getInputStream());
            sc.next();
            return sc.next();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "ERROR";
        }
    }
    
    private static String getOriginalHWID() {
        final Properties props = System.getProperties();
        final String osName = props.getProperty("os.name");
        final String osVersion = props.getProperty("os.version");
        final String osArch = props.getProperty("os.arch");
        final String userName = props.getProperty("user.name");
        final String cpuID = getCpuId();
        final String biosVersion = getBiosVersion();
        final String baseboardManufacturer = getBaseboardManufacturer();
        return osName + osVersion + osArch + userName + cpuID + biosVersion + baseboardManufacturer;
    }
    
    public static String getHWID() {
        final String originalHWID = getOriginalHWID();
        return encode(originalHWID);
    }
    
    public static Boolean checkHWID(final String HWID, final String URL) {
        return ValidationUtils.getHWIDList(URL).contains(HWID);
    }
}
