{
  "Description": "This template deploys an ECS cluster to the provided VPC and subnets  using an Auto Scaling Group\n",
  "Parameters": {
    "EnvironmentName": {
      "Description": "An environment name that will be prefixed to resource names",
      "Type": "String"
    },
    "InstanceType": {
      "Description": "Which instance type should we use to build the ECS cluster?",
      "Type": "String",
      "Default": "c4.large"
    },
    "ClusterSize": {
      "Description": "How many ECS hosts do you want to initially deploy?",
      "Type": "Number",
      "Default": 4
    },
    "VPC": {
      "Description": "Choose which VPC this ECS cluster should be deployed to",
      "Type": "AWS::EC2::VPC::Id"
    },
    "Subnets": {
      "Description": "Choose which subnets this ECS cluster should be deployed to",
      "Type": "List<AWS::EC2::Subnet::Id>"
    },
    "SecurityGroup": {
      "Description": "Select the Security Group to use for the ECS cluster hosts",
      "Type": "AWS::EC2::SecurityGroup::Id"
    }
  },
  "Mappings": {
    "AWSRegionToAMI": {
      "us-east-1": {
        "AMI": "ami-eca289fb"
      },
      "us-east-2": {
        "AMI": "ami-446f3521"
      },
      "us-west-1": {
        "AMI": "ami-9fadf8ff"
      },
      "us-west-2": {
        "AMI": "ami-7abc111a"
      },
      "eu-west-1": {
        "AMI": "ami-a1491ad2"
      },
      "eu-central-1": {
        "AMI": "ami-54f5303b"
      },
      "ap-northeast-1": {
        "AMI": "ami-9cd57ffd"
      },
      "ap-southeast-1": {
        "AMI": "ami-a900a3ca"
      },
      "ap-southeast-2": {
        "AMI": "ami-5781be34"
      }
    }
  },
  "Resources": {
    "ECSCluster": {
      "Type": "AWS::ECS::Cluster",
      "Properties": {
        "ClusterName": "EnvironmentName"
      }
    },
    "ECSAutoScalingGroup": {
      "Type": "AWS::AutoScaling::AutoScalingGroup",
      "Properties": {
        "VPCZoneIdentifier": "Subnets",
        "LaunchConfigurationName": "ECSLaunchConfiguration",
        "MinSize": "ClusterSize",
        "MaxSize": "ClusterSize",
        "DesiredCapacity": "ClusterSize",
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} ECS host",
            "PropagateAtLaunch": true
          }
        ]
      },
      "CreationPolicy": {
        "ResourceSignal": {
          "Timeout": "PT15M"
        }
      },
      "UpdatePolicy": {
        "AutoScalingRollingUpdate": {
          "MinInstancesInService": 1,
          "MaxBatchSize": 1,
          "PauseTime": "PT15M",
          "WaitOnResourceSignals": true
        }
      }
    },
    "ECSLaunchConfiguration": {
      "Type": "AWS::AutoScaling::LaunchConfiguration",
      "Properties": {
        "ImageId": [
          "AWSRegionToAMI",
          "AWS::Region",
          "AMI"
        ],
        "InstanceType": "InstanceType",
        "SecurityGroups": [
          "SecurityGroup"
        ],
        "IamInstanceProfile": "ECSInstanceProfile",
        "UserData": {
          "Fn::Base64": "#!/bin/bash\nyum install -y aws-cfn-bootstrap\n/opt/aws/bin/cfn-init -v --region ${AWS::Region} --stack ${AWS::StackName} --resource ECSLaunchConfiguration\n/opt/aws/bin/cfn-signal -e $? --region ${AWS::Region} --stack ${AWS::StackName} --resource ECSAutoScalingGroup\n"
        }
      },
      "Metadata": {
        "AWS::CloudFormation::Init": {
          "config": {
            "commands": {
              "01_add_instance_to_cluster": {
                "command": "echo ECS_CLUSTER=${ECSCluster} >> /etc/ecs/ecs.config"
              }
            },
            "files": {
              "/etc/cfn/cfn-hup.conf": {
                "mode": 256,
                "owner": "root",
                "group": "root",
                "content": "[main]\nstack=${AWS::StackId}\nregion=${AWS::Region}\n"
              },
              "/etc/cfn/hooks.d/cfn-auto-reloader.conf": {
                "content": "[cfn-auto-reloader-hook]\ntriggers=post.update\npath=Resources.ContainerInstances.Metadata.AWS::CloudFormation::Init\naction=/opt/aws/bin/cfn-init -v --region ${AWS::Region} --stack ${AWS::StackName} --resource ECSLaunchConfiguration\n"
              }
            },
            "services": {
              "sysvinit": {
                "cfn-hup": {
                  "enabled": true,
                  "ensureRunning": true,
                  "files": [
                    "/etc/cfn/cfn-hup.conf",
                    "/etc/cfn/hooks.d/cfn-auto-reloader.conf"
                  ]
                }
              }
            }
          }
        }
      }
    },
    "ECSRole": {
      "Type": "AWS::IAM::Role",
      "Properties": {
        "Path": "/",
        "RoleName": "${EnvironmentName}-ECSRole-${AWS::Region}",
        "AssumeRolePolicyDocument": "{\n    \"Statement\": [{\n        \"Action\": \"sts:AssumeRole\",\n        \"Effect\": \"Allow\",\n        \"Principal\": { \n            \"Service\": \"ec2.amazonaws.com\" \n        }\n    }]\n}\n",
        "Policies": [
          {
            "PolicyName": "ecs-service",
            "PolicyDocument": "{\n    \"Statement\": [{\n        \"Effect\": \"Allow\",\n        \"Action\": [\n            \"ecs:CreateCluster\",\n            \"ecs:DeregisterContainerInstance\",\n            \"ecs:DiscoverPollEndpoint\",\n            \"ecs:Poll\",\n            \"ecs:RegisterContainerInstance\",\n            \"ecs:StartTelemetrySession\",\n            \"ecs:Submit*\",\n            \"logs:CreateLogStream\",\n            \"logs:PutLogEvents\",\n            \"ecr:BatchCheckLayerAvailability\",\n            \"ecr:BatchGetImage\",\n            \"ecr:GetDownloadUrlForLayer\",\n            \"ecr:GetAuthorizationToken\"\n        ],\n        \"Resource\": \"*\"\n    }]\n}\n"
          }
        ]
      }
    },
    "ECSInstanceProfile": {
      "Type": "AWS::IAM::InstanceProfile",
      "Properties": {
        "Path": "/",
        "Roles": [
          "ECSRole"
        ]
      }
    }
  },
  "Outputs": {
    "Cluster": {
      "Description": "A reference to the ECS cluster",
      "Value": "ECSCluster"
    }
  }
}
