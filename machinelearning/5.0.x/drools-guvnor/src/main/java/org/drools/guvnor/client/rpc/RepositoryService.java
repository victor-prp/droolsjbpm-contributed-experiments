package org.drools.guvnor.client.rpc;
/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.Date;
import java.util.List;
import java.util.Map;

import org.drools.guvnor.client.modeldriven.SuggestionCompletionEngine;
import org.drools.guvnor.client.modeldriven.testing.Scenario;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializableException;

/**
 * This is what the remote service will implement, as a servlet.
 * (in hosted/debug mode, you could also use an implementation that was in-process).
 */
public interface RepositoryService extends RemoteService {

    /**
     * @param categoryPath A "/" delimited path to a category.
     * @param callback
     */
    public String[] loadChildCategories(String categoryPath);

    /**
     * Return a a 2d array/grid of results for rules.
     * @param A "/" delimited path to a category.
     */
    public TableDataResult loadRuleListForCategories(String categoryPath, int skip, int numRows, String tableConfig) throws SerializableException;

    /**
     * Return a a 2d array/grid of results for rules.
     * @param The name of the state.
     */
    public TableDataResult loadRuleListForState(String state, int skip, int numRows, String tableConfig) throws SerializableException;


    /**
     * This will return a TableConfig of header names.
     * @param listName The name of the list that we are going to render.
     */
    public TableConfig loadTableConfig(String listName);

    /**
     * This will create a new category at the specified path.
     */
    public Boolean createCategory(String path, String name, String description);

    /**
     * Creates a brand new rule with the initial category.
     * Return the UUID of the item created.
     * This will not check in the rule, but just leave it as saved in the repo.
     */
    public String createNewRule(String ruleName, String description, String initialCategory, String initialPackage, String format) throws SerializableException;

    /**
     * Delete un checked in Asset
     */
    public void deleteUncheckedRule(String ruleName, String initialPackage);


    /**
     * Clear the rules repositoty, Use at your own risk.
     */
    public void clearRulesRepository();

    /**
     * This returns a list of packages where rules may be added.
     * Only the UUID and the name need to be populated.
     */
    public PackageConfigData[] listPackages();

    /**
     * This returns a list of archived packages.
     */
    public PackageConfigData[] listArchivedPackages();


    /**
     * This loads up all the stuff for a
     * rule asset based on the UUID (always latest and editable version).
     */
    public RuleAsset loadRuleAsset(String UUID) throws SerializableException;


    /**
     * This will load the history of the given asset, in a summary format suitable
     * for display in a table.
     */
    public TableDataResult loadAssetHistory(String uuid) throws SerializableException;


    /**
     * This will load all archived assets, in a summary format suitable
     * for display in a table.
     */

    public TableDataResult loadArchivedAssets(int skip, int numRows) throws SerializableException;


    /**
     * This checks in a new version of an asset.
     * @return the UUID of the asset you are checking in,
     * null if there was some problem (and an exception was not thrown).
     */
    public String checkinVersion(RuleAsset asset) throws SerializableException;


    /**
     * This will restore the specified version in the repository, saving, and creating
     * a new version (with all the restored content).
     */
    public void restoreVersion(String versionUUID, String assetUUID, String comment);

    /**
     * This creates a package of the given name, and checks it in.
     * @return UUID of the created item.
     */
    public String createPackage(String name, String description) throws SerializableException;

    /**
     * Loads a package by its uuid.
     * @return Well, its pretty obvious if you think about it for a minute. Really.
     */
    public PackageConfigData loadPackageConfig(String uuid);

    /**
     * Saves the package config data in place (does not create a new version of anything).
     * @return A ValidatedReponse, with any errors to be reported.
     * No payload is in the response. If there are any errors, the user
     * should be given the option to review them, and correct them if needed
     * (but a save will not be prevented this way - as its not an exception).
     */
    public ValidatedResponse savePackage(PackageConfigData data) throws SerializableException;


    /**
     * Given a format, this will return assets that match.
     * It can also be used for "pagination" by passing in start and
     * finish row numbers.
     * @param packageUUID The package uuid to search inside.
     * @param format The format to filter on. If this is empty - it will look for all non "known" asset types (ie "misc" stuff).
     * @param numRows The number of rows to return. -1 means all.
     * @param startRow The starting row number if paging - if numRows is -1 then this is ignored.
     */
    public TableDataResult listAssets(String packageUUID, String formats[], int skip, int numRows, String tableConfig) throws SerializableException;

    /**
     * Returns a list of valid states.
     */
    public String[] listStates() throws SerializableException;

    /**
     * Create a state (status).
     * @return the UUID of the created StateItem.
     */
    public String createState(String name) throws SerializableException;
    
    /**
     * Renames a state.
     * @param oldName states old name.
     * @param newName states new name.
     * @throws SerializableException
     */
    public void renameState(String oldName, String newName) throws SerializableException;
    
    /**
     * Removes a state.
     * @param name state name that will be removed.
     * @throws SerializableException
     */
    public void removeState(String name) throws SerializableException;


    /**
     * This will change the state of an asset or package.
     * @param uuid The UUID of the asset we are tweaking.
     * @param newState The new state to set. It must be valid in the repo.
     * @param wholePackage true if it is a package we are setting the state of.
     * If this is true, UUID must be the status of a package, if false, it must be an asset.
     */
    public void changeState(String uuid, String newState, boolean wholePackage);

    /**
     * This moves an asset to the given target package.
     */
    public void changeAssetPackage(String uuid, String newPackage, String comment);

    /**
     * Copies an asset into a new destination package.
     * @param assetUUID The source assetID.
     * @param newPackage The destination package (may be the same as the current source package, but
     * in that case the asset has to have a different name).
     * @param newName The new name of the asset.
     */
    public String copyAsset(String assetUUID, String newPackage, String newName);


    /**
     * Copy the package (everything).
     * @param sourcePackageName
     * @param destPackageName
     */
    public void copyPackage(String sourcePackageName, String destPackageName) throws SerializableException;

    /**
     * This will load a list of snapshots for the given package. Snapshots are created
     * by taking a labelled copy of a package, at a point in time, for instance for deployment.
     */
    public SnapshotInfo[] listSnapshots(String packageName);


    /**
     * Create a package snapshot for deployment.
     * @param packageName THe name of the package to copy.
     * @param snapshotName The name of the snapshot. Has to be unique unless existing one is to be replaced.
     * @param replaceExisting Replace the existing one (must be true to replace an existing snapshot of the same name).
     * @param comment A comment to be added to the copied one.
     */
    public void createPackageSnapshot(String packageName, String snapshotName, boolean replaceExisting, String comment);

    /**
     * This alters an existing snapshot, it can be used to copy or delete it.
     * @param packageName The package name that we are dealing with.
     * @param snapshotName The snapshot name (this must exist)
     * @param delete true if the snapshotName is to be removed.
     * @param newSnapshotName The name of the target snapshot that the contents will be copied to.
     */
    public void copyOrRemoveSnapshot(String packageName, String snapshotName, boolean delete, String newSnapshotName) throws SerializableException;


    /**
     * This will quickly return a list of asset names/descriptions.
     * This list will be limited, and it will be flagged if more are found then can be shown.
     * Returning an extra empty row means there are more to come...
     *
     * The id of a row is the UUID, the first value is the name, the next the description.
     * Finally, if there is "more" rows, a row will be returned which has "MORE" as its ID.
     *
     */
    public TableDataResult quickFindAsset(String searchText, int maxMatches, boolean searchArchived);


    /**
     * This will remove a category. A category must have no
     * current assets linked to it, or else it will not be able to be removed.
     * @param categoryPath The full path to the category. Any sub categories will also
     * be removed.
     * @throws SerializableException For when it all goes horribly wrong.
     */
    public void removeCategory(String categoryPath) throws SerializableException;

    /**
     * Loads up the SuggestionCompletionEngine for the given package.
     * As this doesn't change that often, its safe to cache. However, if a change is made to
     * a package, should blow away the cache.
     */
    public SuggestionCompletionEngine loadSuggestionCompletionEngine(String packageName) throws SerializableException;

    /**
     * Build the package (may be a snapshot) and return the result.
     *
     * This will then store the result in the package as an attachment.
     *
     * if a non null selectorName is passed in it will lookup a selector as configured
     * in the systems selectors.properties file. This will then apply the filter to the
     * package being built.
     */
    public BuilderResult[] buildPackage(String packageUUID, String selectorName, boolean force) throws SerializableException;

    /**
     * This will return the effective DRL for a package.
     * This would be the equivalent if all the rules were written by hand in the one file.
     * It may not actually be compiled this way in the implementation, so this is for display and
     * debugging assistance only.
     *
     * It should still generate
     *
     * @throws SerializableException
     */
    public String buildPackageSource(String packageUUID) throws SerializableException;


    /**
     * This will return the effective source for an asset (in DRL).
     * Used as an aid for debugging.
     */
    public String buildAssetSource(RuleAsset asset) throws SerializableException;

    /**
     * This will build the asset and return any build results (errors).
     * This is only to report on the results - it will generally not store any state or apply any changed.
     */
    public BuilderResult[] buildAsset(RuleAsset asset) throws SerializableException;

    /**
     * Rename an asset.
     */
    public String renameAsset(String uuid, String newName);

    /**
     * Rename a category - taking in the full path, and just the new name.
     */
    public void renameCategory(String fullPathAndName, String newName);

    /**
     * Archive asset based on uuid
     * @param uuid
     */
    public void archiveAsset(String uuid, boolean value );

    /**
     * Remove an asset based on uuid
     * @param uuid
     */
    public void removeAsset(String uuid);

    /**
     * Permanently remove a package (delete it).
     * @param uuid of the package.
     */
    public void removePackage(String uuid);

    /**
     * Rename a package.
     */
    public String renamePackage(String uuid, String newName);

    /**
     * This will force a rebuild of all snapshots binary data.
     * No errors are expected, as there will be no change. If there are errors,
     * an expert will need to look at them.
     */
    public void rebuildSnapshots() throws SerializableException;


    /**
     * This will force a rebuild of all packages binary data.
     * No errors are expected, as there will be no change. If there are errors,
     * an expert will need to look at them.
     */
    public void rebuildPackages() throws SerializableException;



    /**
     * This will list the rules available in a package.
     * This has an upper limit of what it will return (it just doesn't make sense to show a list of 20K items !).
     */
    public String[] listRulesInPackage(String packageName) throws SerializableException;

    /**
     *
     * @param packageName The package name the scenario is to be run in.
     * @param scenario The scenario to run.
     * @return The scenario, with the results fields populated.
     * @throws SerializableException
     */
    public SingleScenarioResult runScenario(String packageName, Scenario scenario) throws SerializableException;


    /**
     * This should be pretty obvious what it does !
     */
    public BulkTestRunResult runScenariosInPackage(String packageUUID) throws SerializableException;

    /**
     * Analyse the package and get a report for it.
     */
    public AnalysisReport analysePackage(String packageUUID) throws SerializableException;


    /**
     * List the fact types (class names) in the scope of a given package.
     * This may not include things on the "system" classpath, but only things specifically scoped to the package
     * (eg in jars that have been uploaded to it as an asset).
     */
    public String[] listTypesInPackage(String packageUUID) throws SerializableException;

    /**
     * This will list the last N log entryies logged by the server. For debugging purposes in the GUI.
     */
    public LogEntry[] showLog();


    /**
     * @param valuePairs key=value pairs to be interpolated into the expression.
     * @param expression The expression, which will then be eval'ed to generate a String[]
     */
    public String[] loadDropDownExpression(String[] valuePairs, String expression);

    /**
     * Runs a full text search using JCR.
     * @param text
     * @param seekArchived
     * @param skip
     * @param numRows
     * @return
     * @throws SerializableException
     */
    public TableDataResult queryFullText(String text, boolean seekArchived, int skip, int numRows) throws SerializableException;

    /**
     * Run a meta data search. All dates are in format as configured for the system. Pass in null and they will not be included in the search (that
     * applies to any field).
     * @param qr
     * @param createdAfter
     * @param createdBefore
     * @param modifiedAfter
     * @param modifiedBefore
     * @param seekArchived
     * @param skip
     * @param numRows
     * @return
     * @throws SerializableException
     */
    public TableDataResult queryMetaData(final MetaDataQuery[] qr, Date createdAfter, Date createdBefore, Date modifiedAfter, Date modifiedBefore,
    		boolean seekArchived, int skip, int numRows) throws SerializableException;


    /**
     * @return A map of username : list of permission types for display reasons.
     */
    public Map<String, List<String>> listUserPermissions() throws DetailedSerializableException;

    /**
     * Loads the user permissions.
     * @param userName
     * @return A map of permission type to the targets it applies to.
     */
    public Map<String, List<String>> retrieveUserPermissions(String userName);

    /**
     * Update the user permissions - takes the userName, and a map from permission type to the list of targets it applies to.
     */
    public void updateUserPermissions(String userName, Map<String, List<String>> perms);


    /**
     * List the available permission types.
     * @return
     */
    public String[] listAvailablePermissionTypes();

    /**
     * Removes user security data.
     */
    public void deleteUser(String userName);
    

    /**
     * Returns the lockers user name
     * @param uuid
     * @return Lockers user name or null if there is no lock.
     */
    public String getAssetLockerUserName(String uuid);

    /**
     * Locks the asset, if a lock already exists this over writes it.
     * @param uuid
     */
    public void lockAsset(String uuid);

    /**
     * Unlocks the asset.
     * @param uuid
     */
    public void unLockAsset(String uuid);

}