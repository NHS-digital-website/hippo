package uk.nhs.digital.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "uk.nhs.digital")
public class PackageRulesTest {

//    @ArchTest
//    public static void noCyclicDependenciesWithinPackages(JavaClasses importedClasses) {
//        slices()
//            .matching("uk.nhs.digital.(*)..")
//            .should().beFreeOfCycles()
//            .check(importedClasses);
//    }


}
