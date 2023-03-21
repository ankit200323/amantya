#!/bin/bash

current_working_dir=$(cd "$(dirname "$0")" || exit; pwd -P)


# branch_name Variable: this variable contains the branch name which will used to do code checkout

branch_name="main"


# bitbucket_username Variable: this variable contains the username which will used to do code checkout

##################################################################################

#Step1 Create dynamic directory to do git checkout                               #

##################################################################################


# repo_url Variable:  this variable contains the complete git repo url

repo_url="https://github.com/5GRealityLab/do_dashboard_ui.git"

sudo su - << EOF
cd
git clone -b "$branch_name" "$repo_url"

cd "do_dashboard_ui/vrsu-helmchart"


#set -x

SCRIPTPATH=$(cd "$(dirname "$0")"; pwd -P)
for d in vrsubackend  vrsufrontend;
do
    cd ""$SCRIPTPATH"/do_dashboard_ui/vrsu-helmchart"

    pwd

    chart_name=$d

    echo $d

    helm status $chart_name

    if [ $? -ne 0 ] ; then
	echo "Installing $chart_name node."
	helm install $chart_name $chart_name
    else
	echo "Node $chart_name already exists. Trying to upgrade."
	helm upgrade $chart_name $chart_name
    fi

done

rm -rf do_dashboard_ui

EOF
