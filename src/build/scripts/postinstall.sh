#!/bin/sh

GETENT_USER=$(getent passwd dragoon)
GETENT_GROUP=$(getent group dragoon)

# Create the dragoon user if it doesn't already exist
if [ "$GETENT_USER" = "" ]; then
	useradd -r dragoon
else
	echo "The 'dragoon' user already exists, skipping creation."
fi

# Create the dragoon group if it doesn't already exist
if [ "$GETENT_GROUP" = "" ]; then
	groupadd dragoon
	usermod -aG dragoon dragoon
else
	echo "The 'dragoon' group already exists, skipping creation."
fi

# Change the directory ownership of /opt and /etc
chown -R dragoon:dragoon /etc/dragoon
chown -R dragoon:dragoon /opt/dragoon
