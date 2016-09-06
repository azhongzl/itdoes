<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ehcache>
<ehcache updateCheck="false" name="hibernateCache">
	<!-- http://ehcache.org/ehcache.xml -->
	<diskStore path="java.io.tmpdir/ehcache/erp/hibernate" />

	<!-- Default cache setting -->
	<defaultCache maxEntriesLocalHeap="10000" eternal="false" timeToIdleSeconds="300" timeToLiveSeconds="600"
		overflowToDisk="true" maxEntriesLocalDisk="100000" />

	<!-- Specific cache setting -->
<#list config.itemList as item>
	<cache name="${item.name}" maxEntriesLocalHeap="${item.maxEntriesLocalHeap}" eternal="${item.eternal}" overflowToDisk="${item.overflowToDisk}"
		maxEntriesLocalDisk="${item.maxEntriesLocalDisk}" />
</#list>
</ehcache>