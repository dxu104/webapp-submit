#!/bin/bash
sudo systemctl daemon-reload
sudo systemctl start JavaService
sudo systemctl enable JavaService
