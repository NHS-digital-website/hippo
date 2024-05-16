<#ftl output_format="HTML">

<div class="grid-row">
    <div class="column column--two-thirds">
        <div class="article-section article-section--summary">
            <h1>Table</h1>
            <div class="article-section--summary">
                <p>Use the table to compare tabular data.</p>
            </div>

            <h2>Table without header</h2>
            <table style="width:100%">
                <tbody>
                    <tr>
                        <td><strong>File contents</strong></td>
                        <td><strong>File</strong></td>
                        <td><strong>Date uploaded</strong></td>
                        <td><strong>Quarterly/Monthly</strong></td>
                    </tr>
                    <tr>
                        <td>Optical Headquarters</td>
                        <td><a href="https://files.digital.nhs.uk/assets/ods/current/eopthq.zip">eopthq</a></td>
                        <td>30 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>Optical sites</td>
                        <td><a href="https://files.digital.nhs.uk/assets/ods/current/eoptsite.zip">eoptsite</a></td>
                        <td>30 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>General Dental Practices</td>
                        <td><a href="https://files.digital.nhs.uk/assets/ods/current/egdpprac.zip">egdpprac</a></td>
                        <td>30 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>General Dental Practitioners</td>
                        <td><a href="https://files.digital.nhs.uk/assets/ods/current/egdp.zip">egdp</a></td>
                        <td>25 Nov 2016</td>
                        <td>Quarterly</td>
                    </tr>
                </tbody>
            </table>

            <hr>

            <h2>Table with header (sortable)</h2>
            <div class="table-wrapper">
            <table style="width:100%">
                <thead>
                    <tr>
                        <th>File contents</th>
                        <th>File</th>
                        <th>Date uploaded</th>
                        <th>Quarterly/Monthly</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Optical Headquarters</td>
                        <td><a href="#">eopthq</a></td>
                        <td>30 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>Optical sites</td>
                        <td><a href="#">eoptsite</a></td>
                        <td>30 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>Archived&nbsp;Successor Organisations</td>
                        <td><a href="#">succarc</a></td>
                        <td>10 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>English Hospital Consultants</td>
                        <td><a href="#">econcur</a></td>
                        <td>14 December 2018</td>
                        <td>Monthly</td>
                    </tr>
                    <tr>
                        <td>English Dental Consultants</td>
                        <td><a href="#">edconcur</a></td>
                        <td>14 December 2018</td>
                        <td>Monthly</td>
                    </tr>
                    <tr>
                        <td>Welsh&nbsp;Hospital Consultants</td>
                        <td><a href="#">wconcur</a></td>
                        <td>24 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                    <tr>
                        <td>Default codes</td>
                        <td><a href="#">default</a></td>
                        <td>29 November 2018</td>
                        <td>Quarterly</td>
                    </tr>
                </tbody>
            </table>
            </div>

        </div>
    </div>
</div>

<#include "../../../../src/js/table-sort.js.ftl" />
<#include "../../../../src/js/table-sort-date.js.ftl" />
<#include "../../../../src/js/init-table-sort.js.ftl" />
