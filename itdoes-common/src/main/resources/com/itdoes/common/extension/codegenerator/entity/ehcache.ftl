<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ehcache>
<ehcache<#if config.name??> name="${config.name}"</#if> updateCheck="false">
	<!-- http://ehcache.org/ehcache.xml -->
<#if config.diskStore??>
<#if config.diskStore.path??>
	<diskStore path="${config.diskStore.path}" />
</#if>
</#if>

	<!-- Default cache setting -->
	<defaultCache<#if config.defaultCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${config.defaultCache.maxEntriesLocalHeap}"</#if><#if config.defaultCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${config.defaultCache.maxEntriesLocalDisk}"</#if><#if config.defaultCache.eternal??> eternal="${config.defaultCache.eternal}"</#if><#if config.defaultCache.timeToIdleSeconds??> timeToIdleSeconds="${config.defaultCache.timeToIdleSeconds}"</#if><#if config.defaultCache.timeToLiveSeconds??> timeToLiveSeconds="${config.defaultCache.timeToLiveSeconds}"</#if>>
<#if config.defaultCache.persistence.strategy??>
		<persistence strategy="${config.defaultCache.persistence.strategy}"/>
</#if>
	</defaultCache>

	<!-- Query cache setting -->
<#if config.standardQueryCache??>
	<cache name="${config.standardQueryCache.name}" <#if config.standardQueryCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${config.standardQueryCache.maxEntriesLocalHeap}"</#if><#if config.standardQueryCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${config.standardQueryCache.maxEntriesLocalDisk}"</#if><#if config.standardQueryCache.eternal??> eternal="${config.standardQueryCache.eternal}"</#if><#if config.standardQueryCache.timeToIdleSeconds??> timeToIdleSeconds="${config.standardQueryCache.timeToIdleSeconds}"</#if><#if config.standardQueryCache.timeToLiveSeconds??> timeToLiveSeconds="${config.standardQueryCache.timeToLiveSeconds}"</#if>>
<#if config.standardQueryCache.persistence.strategy??>
		<persistence strategy="${config.standardQueryCache.persistence.strategy}"/>
</#if>
	</cache>
</#if>
<#if config.updateTimestampsCache??>
	<cache name="${config.updateTimestampsCache.name}" <#if config.updateTimestampsCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${config.updateTimestampsCache.maxEntriesLocalHeap}"</#if><#if config.updateTimestampsCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${config.updateTimestampsCache.maxEntriesLocalDisk}"</#if><#if config.updateTimestampsCache.eternal??> eternal="${config.updateTimestampsCache.eternal}"</#if><#if config.updateTimestampsCache.timeToIdleSeconds??> timeToIdleSeconds="${config.updateTimestampsCache.timeToIdleSeconds}"</#if><#if config.updateTimestampsCache.timeToLiveSeconds??> timeToLiveSeconds="${config.updateTimestampsCache.timeToLiveSeconds}"</#if>>
<#if config.updateTimestampsCache.persistence.strategy??>
		<persistence strategy="${config.updateTimestampsCache.persistence.strategy}"/>
</#if>
	</cache>
</#if>

	<!-- Entity cache setting -->
<#list config.cacheList as cache>

	<cache name="${cache.name}"<#if cache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${cache.maxEntriesLocalHeap}"</#if><#if cache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${cache.maxEntriesLocalDisk}"</#if><#if cache.eternal??> eternal="${cache.eternal}"</#if><#if cache.timeToIdleSeconds??> timeToIdleSeconds="${cache.timeToIdleSeconds}"</#if><#if cache.timeToLiveSeconds??> timeToLiveSeconds="${cache.timeToLiveSeconds}"</#if>>
<#if cache.persistence.strategy??>
		<persistence strategy="${cache.persistence.strategy}"/>
</#if>
	</cache>
</#list>
</ehcache>