// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils.Verification;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ValidationUtils
{
    public static List<String> getHWIDList(final String URL) {
        final List<String> HWIDList = new ArrayList<String>();
        try {
            final URL url = new URL(URL);
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                HWIDList.add(inputLine);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return HWIDList;
    }
}
