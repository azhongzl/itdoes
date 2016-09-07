<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ehcache>
<ehcache<#if config.name??> name="${config.name}"</#if> updateCheck="false">
	<!-- http://ehcache.org/ehcache.xml -->
<#if config.diskStore.path??>
	<diskStore path="${config.diskStore.path}" />
</#if>

	<!-- Default cache setting -->
	<defaultCache<#if config.defaultCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${config.defaultCache.maxEntriesLocalHeap}"</#if><#if config.defaultCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${config.defaultCache.maxEntriesLocalDisk}"</#if><#if config.defaultCache.eternal??> eternal="${config.defaultCache.eternal}"</#if><#if config.defaultCache.timeToIdleSeconds??> timeToIdleSeconds="${config.defaultCache.timeToIdleSeconds}"</#if><#if config.defaultCache.timeToLiveSeconds??> timeToLiveSeconds="${config.defaultCache.timeToLiveSeconds}"</#if>>
<#if config.defaultCache.persistence.strategy??>
		<persistence strategy="${config.defaultCache.persistence.strategy}"/>
</#if>
	</defaultCache>
<#list config.cacheList as cache>

	<cache name="${cache.name}"<#if cache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${cache.maxEntriesLocalHeap}"</#if><#if cache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${cache.maxEntriesLocalDisk}"</#if><#if cache.eternal??> eternal="${cache.eternal}"</#if><#if cache.timeToIdleSeconds??> timeToIdleSeconds="${cache.timeToIdleSeconds}"</#if><#if cache.timeToLiveSeconds??> timeToLiveSeconds="${cache.timeToLiveSeconds}"</#if>>
<#if cache.persistence.strategy??>
		<persistence strategy="${cache.persistence.strategy}"/>
</#if>
	</cache>
</#list>
</ehcache>