<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text"/>
  <xsl:template name="format-date">

    <xsl:param name="date" />
    <xsl:param name="part" />
    <xsl:param name="dateSplitter">-</xsl:param>
    <xsl:param name="timeSplitter">T</xsl:param>
    <xsl:param name="format">DMY</xsl:param>

    <xsl:variable name="dateonly" select="substring-before($date, 'T')" />
    <xsl:variable name="year" select="substring-before($dateonly, $dateSplitter)" />
    <xsl:variable name="month" select="substring-before(substring-after($dateonly, $dateSplitter), $dateSplitter)" />
    <xsl:variable name="day" select="substring-after(substring-after($dateonly, $dateSplitter), $dateSplitter)" />
    <xsl:variable name="monthName" select="substring(substring-after('01Jan02Feb03Mar04Apr05May06Jun07Jul08Aug09Sep10Oct11Nov12Dec', $month), 1, 3)" />
    <xsl:variable name="timeonly" select="substring-after($date, 'T')" />

    <xsl:choose>
      <xsl:when test="$format = 'DMY'">
        <xsl:choose>
          <xsl:when test="$part = 'both'">
            <xsl:value-of select="concat($day, $dateSpliter, $month, $dateSplitter, $year, $timeSplitter, $timeonly)" />
          </xsl:when>
          <xsl:when test="$part = 'date'">
            <xsl:value-of select="concat($day, $dateSplitter, $month, $dateSplitter, $year)" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$timeonly" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$format = 'YMD'">
        <xsl:choose>
          <xsl:when test="$part = 'both'">
            <xsl:value-of select="concat($year, $dateSplitter, $month, $dateSplitter, $day, $timeSplitter, $timeonly)" />
          </xsl:when>
          <xsl:when test="$part = 'date'">
            <xsl:value-of select="concat($year, $dateSplitter, $month, $dateSplitter, $day)" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$timeonly" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="replace-string">
      <xsl:param name="text"/>
      <xsl:param name="replace"/>
      <xsl:param name="with"/>
      <xsl:choose>
	<xsl:when test="contains($text,$replace)">
	  <xsl:value-of select="substring-before($text,$replace)"/>
	  <xsl:value-of select="$with"/>
	  <xsl:call-template name="replace-string">
	    <xsl:with-param name="text" select="substring-after($text,$replace)"/>
	    <xsl:with-param name="replace" select="$replace"/>
	    <xsl:with-param name="with" select="$with"/>
	  </xsl:call-template>
	</xsl:when>
	<xsl:otherwise>
	  <xsl:value-of select="$text"/>
	</xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template match="/">
  \begin{longtable}{l l l p{80px} p{80px}}
	  \textbf{Rev} &amp; \textbf{Autor}  &amp;\textbf{Datum} &amp; \textbf{Beschreibung} &amp; \textbf{Dateien}\\
        <!--           &amp;      \          &amp;      \        &amp;          \            &amp;                 \\-->
	  \hline
	               &amp;      \          &amp;      \        &amp;          \            &amp;                 \\
	  <xsl:for-each select="log/logentry">
		  <xsl:sort select="@revision" data-type="number" order="descending"/>
		  <xsl:value-of select="@revision"/> &amp;
		  <xsl:call-template name="replace-string">
		      <xsl:with-param name="text" select="author/text()"/>
		      <xsl:with-param name="replace" select="'\'" />
		      <xsl:with-param name="with" select="'{\textbackslash}'" />
		  </xsl:call-template>
		  &amp;
		  <xsl:call-template name="format-date">
		      <xsl:with-param name="date" select="date/text()"/>
		      <xsl:with-param name="part" select="'date'" />
		  </xsl:call-template>
		  &amp;
		  <xsl:call-template name="replace-string">
			  <xsl:with-param name="text">
				  <xsl:call-template name="replace-string">
					  <xsl:with-param name="text">
						  <xsl:call-template name="replace-string">
							  <xsl:with-param name="text" select="msg/text()"/>
							  <xsl:with-param name="replace" select="'\'" />
							  <xsl:with-param name="with" select="'{\textbackslash}'" />
						  </xsl:call-template>
				  </xsl:with-param>
					  <xsl:with-param name="replace" select="'&amp;'" />
					  <xsl:with-param name="with" select="'\&amp;'" />
				  </xsl:call-template>
			  </xsl:with-param>
			  <xsl:with-param name="replace" select="'_'" />
			  <xsl:with-param name="with" select="'\_'" />
		  </xsl:call-template>
		  &amp; \strut
		  <xsl:for-each select="paths/path">
		    <xsl:if test="boolean(@kind = 'file')">
		      <xsl:call-template name="replace-string">
			      <xsl:with-param name="text">
				      <xsl:call-template name="replace-string">
					      <xsl:with-param name="text">
						      <xsl:call-template name="replace-string">
							      <xsl:with-param name="text" select="substring(text(),11)"/>
							      <xsl:with-param name="replace" select="'\'" />
							      <xsl:with-param name="with" select="'{\textbackslash}'" />
						      </xsl:call-template>
					      </xsl:with-param>
					      <xsl:with-param name="replace" select="'&amp;'" />
					      <xsl:with-param name="with" select="'\&amp;'" />
				      </xsl:call-template>
			      </xsl:with-param>
			      <xsl:with-param name="replace" select="'_'" />
			      <xsl:with-param name="with" select="'\_'" />
		      </xsl:call-template> \par <!--\\ &amp; \ &amp;\  &amp;\ &amp;-->
		    </xsl:if>
		  </xsl:for-each> \strut \\
	  </xsl:for-each>
  \end{longtable}
  </xsl:template>
</xsl:stylesheet>
