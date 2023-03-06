# /********************************************************************************
# * Copyright (c) 2023 Contributors to the Eclipse Foundation
# *
# * See the NOTICE file(s) distributed with this work for additional
# * information regarding copyright ownership.
# *
# * This program and the accompanying materials are made available under the
# * terms of the Apache License 2.0 which is available at
# * https://www.apache.org/licenses/LICENSE-2.0
# *
# * SPDX-License-Identifier: Apache-2.0
# ********************************************************************************/

SUMMARY = "Airgapped Container Installer"
DESCRIPTION = "A service that installs pre-downloaded container images in a specified directory"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = " file://airgap-ctr-installer"
SRC_URI:append = " file://airgap-container-installer.service.template"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${THISDIR}/files/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PREINSTALLED_CTR_IMAGES_DIR ??= "/var/constainers/images"
AG_SERVICE_DIR = "${systemd_unitdir}/system"

install_service() {
    install -d ${D}${AG_SERVICE_DIR}
    install -m 0644 ${WORKDIR}/airgap-container-installer.service.template ${D}${AG_SERVICE_DIR}/airgap-container-installer.service
    bberror ${AD_SERVICE_DIR}
    sed -e 's,@AG_CFG_DD@,${bindir},g' \
        -e 's,@AG_CFG_DD@,${PREINSTALLED_CTR_IMAGES_DIR},g' \
    -i ${D}${AG_SERVICE_DIR}/airgap-container-installer.service
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/airgap-ctr-installer ${D}${bindir}
    install_service
}

FILES:${PN} += " ${bindir}/airgap-ctr-installer"
FILES:${PN} += " ${AG_SERVICE_DIR}/airgap-container-installer.service"

PACKAGES = "${PN}"
