/*
 * Copyright (C) 2002 Thomas Dudziak, Jan Wloka
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package jast.profiler;
public class ParseInfo
{
    private String _unitName;
    private long   _fileSize        = 0l;
    private long   _astSize         = 0l;
    private long   _resolvedAstSize = 0l;
    private long   _parseTime       = 0l;
    private long   _resolveTime     = 0l;

    public ParseInfo(String unitName)
    {
        _unitName = unitName;
    }

    public long getAstSize()
    {
        return _astSize;
    }

    public long getFileSize()
    {
        return _fileSize;
    }

    public long getParseTime()
    {
        return _parseTime;
    }

    public long getResolvedAstSize()
    {
        return _resolvedAstSize;
    }

    public long getResolveTime()
    {
        return _resolveTime;
    }

    public String getUnitName()
    {
        return _unitName;
    }

    public void setAstSize(long astSize)
    {
        _astSize = astSize;
    }

    public void setFileSize(long fileSize)
    {
        _fileSize = fileSize;
    }

    public void setParseTime(long parseTime)
    {
        _parseTime = parseTime;
    }

    public void setResolvedAstSize(long resolvedAstSize)
    {
        _resolvedAstSize = resolvedAstSize;
    }

    public void setResolveTime(long resolveTime)
    {
        _resolveTime = resolveTime;
    }
}
