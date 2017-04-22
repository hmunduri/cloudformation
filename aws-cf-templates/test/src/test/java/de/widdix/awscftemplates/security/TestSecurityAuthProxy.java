package de.widdix.awscftemplates.security;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Config;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestSecurityAuthProxy extends ACloudFormationTest {

    @Test
    public void testHAGitHubOrga() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String proxyStackName = "auth-proxy-ha-github-orga-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(proxyStackName,
                            "security/auth-proxy-ha-github-orga.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("CertificateArn").withParameterValue(Config.get(Config.Key.ACM_CERTIFICATE_ARN)),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName),
                            new Parameter().withParameterKey("GitHubOrganization").withParameterValue("widdix"), // fake value
                            new Parameter().withParameterKey("GitHubClientId").withParameterValue("2bb8ab97cb147fa499f6"), // fake value
                            new Parameter().withParameterKey("GitHubClientSecret").withParameterValue("d3a1a8a9b6525fb0599d22fc750f17ef76032c62"), // fake value
                            new Parameter().withParameterKey("Upstream").withParameterValue("https://widdix.net/"),
                            new Parameter().withParameterKey("CookieSecret").withParameterValue("ylLjZRVNRlzW7sqyQeERBQ==") // fake value
                    );
                    try {
                        final String domain = this.createDomain(proxyStackName, this.getStackOutputValue(proxyStackName, "DNSName"));
                        final String url = "https://" + domain;
                        final Callable<String> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 403) {
                                throw new RuntimeException("403 expected, but saw " + WS.getStatus(response));
                            }
                            return WS.getResponseAsString(response);
                        };
                        final String response = this.retry(callable);
                        // check if OAuth2 Proxy appears in HTML
                        Assert.assertTrue(response.contains("OAuth2 Proxy"));
                    } finally {
                        this.deleteDomain(proxyStackName);
                    }
                } finally {
                    this.deleteStack(proxyStackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

}
