<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ehcache>
<ehcache<#if name??> name="${name}"</#if> updateCheck="false">
	<!-- http://ehcache.org/ehcache.xml -->
<#if diskStore??>
<#if diskStore.path??>
	<diskStore path="${diskStore.path}" />
</#if>
</#if>

	<!-- Default cache setting -->
	<defaultCache<#if defaultCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${defaultCache.maxEntriesLocalHeap}"</#if><#if defaultCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${defaultCache.maxEntriesLocalDisk}"</#if><#if defaultCache.eternal??> eternal="${defaultCache.eternal}"</#if><#if defaultCache.timeToIdleSeconds??> timeToIdleSeconds="${defaultCache.timeToIdleSeconds}"</#if><#if defaultCache.timeToLiveSeconds??> timeToLiveSeconds="${defaultCache.timeToLiveSeconds}"</#if>>
<#if defaultCache.persistence.strategy??>
		<persistence strategy="${defaultCache.persistence.strategy}"/>
</#if>
	</defaultCache>

	<!-- Query cache setting -->
<#if standardQueryCache??>
	<cache name="${standardQueryCache.name}" <#if standardQueryCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${standardQueryCache.maxEntriesLocalHeap}"</#if><#if standardQueryCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${standardQueryCache.maxEntriesLocalDisk}"</#if><#if standardQueryCache.eternal??> eternal="${standardQueryCache.eternal}"</#if><#if standardQueryCache.timeToIdleSeconds??> timeToIdleSeconds="${standardQueryCache.timeToIdleSeconds}"</#if><#if standardQueryCache.timeToLiveSeconds??> timeToLiveSeconds="${standardQueryCache.timeToLiveSeconds}"</#if>>
<#if standardQueryCache.persistence.strategy??>
		<persistence strategy="${standardQueryCache.persistence.strategy}"/>
</#if>
	</cache>
</#if>
<#if updateTimestampsCache??>
	<cache name="${updateTimestampsCache.name}" <#if updateTimestampsCache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${updateTimestampsCache.maxEntriesLocalHeap}"</#if><#if updateTimestampsCache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${updateTimestampsCache.maxEntriesLocalDisk}"</#if><#if updateTimestampsCache.eternal??> eternal="${updateTimestampsCache.eternal}"</#if><#if updateTimestampsCache.timeToIdleSeconds??> timeToIdleSeconds="${updateTimestampsCache.timeToIdleSeconds}"</#if><#if updateTimestampsCache.timeToLiveSeconds??> timeToLiveSeconds="${updateTimestampsCache.timeToLiveSeconds}"</#if>>
<#if updateTimestampsCache.persistence.strategy??>
		<persistence strategy="${updateTimestampsCache.persistence.strategy}"/>
</#if>
	</cache>
</#if>

	<!-- Entity cache setting -->
<#list cacheList as cache>

	<cache name="${cache.name}"<#if cache.maxEntriesLocalHeap??> maxEntriesLocalHeap="${cache.maxEntriesLocalHeap}"</#if><#if cache.maxEntriesLocalDisk??> maxEntriesLocalDisk="${cache.maxEntriesLocalDisk}"</#if><#if cache.eternal??> eternal="${cache.eternal}"</#if><#if cache.timeToIdleSeconds??> timeToIdleSeconds="${cache.timeToIdleSeconds}"</#if><#if cache.timeToLiveSeconds??> timeToLiveSeconds="${cache.timeToLiveSeconds}"</#if>>
<#if cache.persistence.strategy??>
		<persistence strategy="${cache.persistence.strategy}"/>
</#if>
	</cache>
</#list>
</ehcache>