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

# Set-up
    1.spring tool suit 
    IDE https://spring.io/tools/sts/all
    
    2.Mysql 
    https://dev.mysql.com/downloads/installer/ 
    
    Mysql workbench as ide 
    https://downloads.mysql.com/archives/workbench/
    
    set the connection to 3306 port (default port)
    username -> root, password -> 1234 
    
    3.git https://git-scm.com/download
    
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

# Best Practices
### Code Review
        1.Someone need to check the unapproved commits and comment the respective changes in line
        2.These commented changes need to be done or discussed before approving/ a new commit with these changes need to be done.
        3.Indentation,optimization,coding standards need to be followed during these reviews.
        4.Try to minimize code and reuse modules at every possible levels.
        5.Prefer 
        Bootstrap over css
        ANGULARJS over Jquery/Js
        FontAwesome over images