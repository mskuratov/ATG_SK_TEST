#!/bin/sh
#
# Copyright 2011, 2012, Oracle and/or its affiliates. All rights reserved.
# Oracle and Java are registered trademarks of Oracle and/or its 
# affiliates. Other names may be trademarks of their respective owners.
# UNIX is a registered trademark of The Open Group.
# 
# This software and related documentation are provided under a license 
# agreement containing restrictions on use and disclosure and are 
# protected by intellectual property laws. Except as expressly permitted 
# in your license agreement or allowed by law, you may not use, copy, 
# reproduce, translate, broadcast, modify, license, transmit, distribute, 
# exhibit, perform, publish, or display any part, in any form, or by any 
# means. Reverse engineering, disassembly, or decompilation of this 
# software, unless required by law for interoperability, is prohibited.
# The information contained herein is subject to change without notice 
# and is not warranted to be error-free. If you find any errors, please 
# report them to us in writing.
# U.S. GOVERNMENT END USERS: Oracle programs, including any operating 
# system, integrated software, any programs installed on the hardware, 
# and/or documentation, delivered to U.S. Government end users are 
# "commercial computer software" pursuant to the applicable Federal 
# Acquisition Regulation and agency-specific supplemental regulations. 
# As such, use, duplication, disclosure, modification, and adaptation 
# of the programs, including any operating system, integrated software, 
# any programs installed on the hardware, and/or documentation, shall be 
# subject to license terms and license restrictions applicable to the 
# programs. No other rights are granted to the U.S. Government.
# This software or hardware is developed for general use in a variety 
# of information management applications. It is not developed or 
# intended for use in any inherently dangerous applications, including 
# applications that may create a risk of personal injury. If you use 
# this software or hardware in dangerous applications, then you shall 
# be responsible to take all appropriate fail-safe, backup, redundancy, 
# and other measures to ensure its safe use. Oracle Corporation and its 
# affiliates disclaim any liability for any damages caused by use of this 
# software or hardware in dangerous applications.
# This software or hardware and documentation may provide access to or 
# information on content, products, and services from third parties. 
# Oracle Corporation and its affiliates are not responsible for and 
# expressly disclaim all warranties of any kind with respect to 
# third-party content, products, and services. Oracle Corporation and 
# its affiliates will not be responsible for any loss, costs, or damages 
# incurred due to your access to or use of third-party content, products, 
# or services.

WORKING_DIR=`dirname ${0} 2>/dev/null`

. "${WORKING_DIR}/../config/script/set_environment.sh"

RS_PREFIX=$ENDECA_PROJECT_NAME
RS_LANG=$LANGUAGE_ID

echo Schema:
$CAS_ROOT/bin/recordstore-cmd.sh list-active-transactions -a ${RS_PREFIX}_${RS_LANG}_schema
echo ===========
echo Dimvals:
$CAS_ROOT/bin/recordstore-cmd.sh list-active-transactions -a ${RS_PREFIX}_${RS_LANG}_dimvals
echo ===========
echo Precedence Rules:
$CAS_ROOT/bin/recordstore-cmd.sh list-active-transactions -a ${RS_PREFIX}_${RS_LANG}_prules
echo ===========
echo Data:
$CAS_ROOT/bin/recordstore-cmd.sh list-active-transactions -a ${RS_PREFIX}_${RS_LANG}_data
