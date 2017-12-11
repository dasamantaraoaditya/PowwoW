# To Work On Powwow
	1.clone the git repository into ur local machine
		git clone https://YOUR_USER_NAME@bitbucket.org/powwowdevelopers/powwow-dev.git
	2.Open the myapp2 folder into ur working IDE (Recommended Spring STS ide/ eclipse)
        a.import the myapp2 folder as a maven project
        b.update the maven project dependencies
        c.Run as spring boot app
	3.Make some chnages in code and add the files to ur staging ans push it to origin
		a.git status (can see the modified files here)
		b.git add FILE_NAME (can add the files into the stagin instance)
		c.git commit COMMIT_MESSAGE (commiting changes to remote repo)
		d.git push origin BEANCH_Name (pushing the changes to the remote repo mostly 'master')
        
# Tracking On Powwow
### Documentation: https://bitbucket.org/powwowdevelopers/powwow-dev/wiki/
        1.New documentation changes need to commited with commit message DOC-CHANGE
        2.Feel free to correct any existing change if wrong (*discuss before change)
        3.New Wiki Page need to have the business logic independently.
        
### Issues tracking https://bitbucket.org/powwowdevelopers/powwow-dev/issues?status=new&status=open
        1.Only an existing feature misbehavior can be tracked as a bug/issue
        2.Assign any active bugs (*High Priority)
        3.Comment the origin and fix in commit message "BUG_FIX:{Message}"

### Features Tracking https://bitbucket.org/powwowdevelopers/powwow-dev/addon/bitbucket-trello-addon/trello-board
		Note: add urself in this group 
		https://trello.com/invite/powwowdev/7412dbfcfcfe6dd0dfb02637785da35f
        
		1.Any open/to-do features can be assigned 
        2.developer who took the feature need to take responsibility of updating the description/business logic into the wiki if feels needed.
        3.Once took a feature into development make sure to update the trello ticket to doing status
        4.If done with ticket comment a developer to review the code (*once code review is one good to push the commit to origin repo)

