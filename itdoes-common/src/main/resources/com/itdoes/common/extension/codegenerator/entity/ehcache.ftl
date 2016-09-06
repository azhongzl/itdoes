<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ehcache>
<ehcache updateCheck="false" name="hibernateCache">
	<!-- http://ehcache.org/ehcache.xml -->
	<diskStore path="java.io.tmpdir/ehcache/erp/hibernate" />

	<!-- Default cache setting -->
	<defaultCache maxEntriesLocalHeap="10000" eternal="false" timeToIdleSeconds="300" timeToLiveSeconds="600"
		overflowToDisk="true" maxEntriesLocalDisk="100000" />

	<!-- Specific cache setting -->
<#list ehcacheList as ehcache>
	<cache name="${ehcache.name}" maxEntriesLocalHeap="${ehcache.maxEntriesLocalHeap}" eternal="${ehcache.eternal}" overflowToDisk="${ehcache.overflowToDisk}"
		maxEntriesLocalDisk="${ehcache.maxEntriesLocalDisk}" />
</#list>
</ehcache>