package com.github.tvbox.osc.bean;

public class MoreSourceBean {

    private boolean isSelected;
    private boolean isServer = true;
    private String sourceName = "";
    private String sourceUrl = "";

    public final String getSourceName()
    {
        return this.sourceName;
    }

    public final String getSourceUrl()
    {
        return this.sourceUrl;
    }

    public final boolean isSelected()
    {
        return this.isSelected;
    }

    public final boolean isServer()
    {
        return this.isServer;
    }

    public final void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public final void setServer(boolean isServer)
    {
        this.isServer = isServer;
    }

    public final void setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
    }

    public final void setSourceUrl(String sourceUrl)
    {
        this.sourceUrl = sourceUrl;
    }

}
