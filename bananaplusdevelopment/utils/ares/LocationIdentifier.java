// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils.ares;

public class LocationIdentifier
{
    private final String path;
    
    public LocationIdentifier(final String path) {
        this.path = "/assets/bananaPlus/" + path;
    }
    
    public String getPath() {
        return this.path;
    }
}
