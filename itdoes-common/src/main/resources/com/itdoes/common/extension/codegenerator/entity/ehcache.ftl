<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ehcache>
<ehcache<#if name??> name="${name}"</#if> updateCheck="false">
	<!-- http://ehcache.org/ehcache.xml -->
<#if diskStore??>
	${diskStore}
</#if>

	<!-- Default cache setting -->
<#if defaultCache??>
	${defaultCache}
</#if>

	<!-- Query cache setting -->
<#if standardQueryCache??>
	${standardQueryCache}
</#if>
<#if updateTimestampsCache??>
	${updateTimestampsCache}
</#if>

	<!-- Entity cache setting -->
<#list cacheList as cache>

	${cache}
</#list>
</ehcache>