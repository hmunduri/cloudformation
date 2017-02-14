{
  "Description": "This template deploys a VPC, with a pair of public and private subnets spread  across two Availabilty Zones. It deploys an Internet Gateway, with a default  route on the public subnets. It deploys a pair of NAT Gateways (one in each AZ),  and default routes for them in the private subnets.\n",
  "Parameters": {
    "EnvironmentName": {
      "Description": "An environment name that will be prefixed to resource names",
      "Type": "String"
    },
    "VpcCIDR": {
      "Description": "Please enter the IP range (CIDR notation) for this VPC",
      "Type": "String",
      "Default": "10.192.0.0/16"
    },
    "PublicSubnet1CIDR": {
      "Description": "Please enter the IP range (CIDR notation) for the public subnet in the first Availability Zone",
      "Type": "String",
      "Default": "10.192.10.0/24"
    },
    "PublicSubnet2CIDR": {
      "Description": "Please enter the IP range (CIDR notation) for the public subnet in the second Availability Zone",
      "Type": "String",
      "Default": "10.192.11.0/24"
    },
    "PrivateSubnet1CIDR": {
      "Description": "Please enter the IP range (CIDR notation) for the private subnet in the first Availability Zone",
      "Type": "String",
      "Default": "10.192.20.0/24"
    },
    "PrivateSubnet2CIDR": {
      "Description": "Please enter the IP range (CIDR notation) for the private subnet in the second Availability Zone",
      "Type": "String",
      "Default": "10.192.21.0/24"
    }
  },
  "Resources": {
    "VPC": {
      "Type": "AWS::EC2::VPC",
      "Properties": {
        "CidrBlock": "VpcCIDR",
        "Tags": [
          {
            "Key": "Name",
            "Value": "EnvironmentName"
          }
        ]
      }
    },
    "InternetGateway": {
      "Type": "AWS::EC2::InternetGateway",
      "Properties": {
        "Tags": [
          {
            "Key": "Name",
            "Value": "EnvironmentName"
          }
        ]
      }
    },
    "InternetGatewayAttachment": {
      "Type": "AWS::EC2::VPCGatewayAttachment",
      "Properties": {
        "InternetGatewayId": "InternetGateway",
        "VpcId": "VPC"
      }
    },
    "PublicSubnet1": {
      "Type": "AWS::EC2::Subnet",
      "Properties": {
        "VpcId": "VPC",
        "AvailabilityZone": [
          0,
          null
        ],
        "CidrBlock": "PublicSubnet1CIDR",
        "MapPublicIpOnLaunch": true,
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Public Subnet (AZ1)"
          }
        ]
      }
    },
    "PublicSubnet2": {
      "Type": "AWS::EC2::Subnet",
      "Properties": {
        "VpcId": "VPC",
        "AvailabilityZone": [
          1,
          null
        ],
        "CidrBlock": "PublicSubnet2CIDR",
        "MapPublicIpOnLaunch": true,
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Public Subnet (AZ2)"
          }
        ]
      }
    },
    "PrivateSubnet1": {
      "Type": "AWS::EC2::Subnet",
      "Properties": {
        "VpcId": "VPC",
        "AvailabilityZone": [
          0,
          null
        ],
        "CidrBlock": "PrivateSubnet1CIDR",
        "MapPublicIpOnLaunch": false,
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Private Subnet (AZ1)"
          }
        ]
      }
    },
    "PrivateSubnet2": {
      "Type": "AWS::EC2::Subnet",
      "Properties": {
        "VpcId": "VPC",
        "AvailabilityZone": [
          1,
          null
        ],
        "CidrBlock": "PrivateSubnet2CIDR",
        "MapPublicIpOnLaunch": false,
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Private Subnet (AZ2)"
          }
        ]
      }
    },
    "NatGateway1EIP": {
      "Type": "AWS::EC2::EIP",
      "DependsOn": "InternetGatewayAttachment",
      "Properties": {
        "Domain": "vpc"
      }
    },
    "NatGateway2EIP": {
      "Type": "AWS::EC2::EIP",
      "DependsOn": "InternetGatewayAttachment",
      "Properties": {
        "Domain": "vpc"
      }
    },
    "NatGateway1": {
      "Type": "AWS::EC2::NatGateway",
      "Properties": {
        "AllocationId": "NatGateway1EIP.AllocationId",
        "SubnetId": "PublicSubnet1"
      }
    },
    "NatGateway2": {
      "Type": "AWS::EC2::NatGateway",
      "Properties": {
        "AllocationId": "NatGateway2EIP.AllocationId",
        "SubnetId": "PublicSubnet2"
      }
    },
    "PublicRouteTable": {
      "Type": "AWS::EC2::RouteTable",
      "Properties": {
        "VpcId": "VPC",
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Public Routes"
          }
        ]
      }
    },
    "DefaultPublicRoute": {
      "Type": "AWS::EC2::Route",
      "DependsOn": "InternetGatewayAttachment",
      "Properties": {
        "RouteTableId": "PublicRouteTable",
        "DestinationCidrBlock": "0.0.0.0/0",
        "GatewayId": "InternetGateway"
      }
    },
    "PublicSubnet1RouteTableAssociation": {
      "Type": "AWS::EC2::SubnetRouteTableAssociation",
      "Properties": {
        "RouteTableId": "PublicRouteTable",
        "SubnetId": "PublicSubnet1"
      }
    },
    "PublicSubnet2RouteTableAssociation": {
      "Type": "AWS::EC2::SubnetRouteTableAssociation",
      "Properties": {
        "RouteTableId": "PublicRouteTable",
        "SubnetId": "PublicSubnet2"
      }
    },
    "PrivateRouteTable1": {
      "Type": "AWS::EC2::RouteTable",
      "Properties": {
        "VpcId": "VPC",
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Private Routes (AZ1)"
          }
        ]
      }
    },
    "DefaultPrivateRoute1": {
      "Type": "AWS::EC2::Route",
      "Properties": {
        "RouteTableId": "PrivateRouteTable1",
        "DestinationCidrBlock": "0.0.0.0/0",
        "NatGatewayId": "NatGateway1"
      }
    },
    "PrivateSubnet1RouteTableAssociation": {
      "Type": "AWS::EC2::SubnetRouteTableAssociation",
      "Properties": {
        "RouteTableId": "PrivateRouteTable1",
        "SubnetId": "PrivateSubnet1"
      }
    },
    "PrivateRouteTable2": {
      "Type": "AWS::EC2::RouteTable",
      "Properties": {
        "VpcId": "VPC",
        "Tags": [
          {
            "Key": "Name",
            "Value": "${EnvironmentName} Private Routes (AZ2)"
          }
        ]
      }
    },
    "DefaultPrivateRoute2": {
      "Type": "AWS::EC2::Route",
      "Properties": {
        "RouteTableId": "PrivateRouteTable2",
        "DestinationCidrBlock": "0.0.0.0/0",
        "NatGatewayId": "NatGateway2"
      }
    },
    "PrivateSubnet2RouteTableAssociation": {
      "Type": "AWS::EC2::SubnetRouteTableAssociation",
      "Properties": {
        "RouteTableId": "PrivateRouteTable2",
        "SubnetId": "PrivateSubnet2"
      }
    }
  },
  "Outputs": {
    "VPC": {
      "Description": "A reference to the created VPC",
      "Value": "VPC"
    },
    "PublicSubnets": {
      "Description": "A list of the public subnets",
      "Value": [
        ",",
        [
          "PublicSubnet1",
          "PublicSubnet2"
        ]
      ]
    },
    "PrivateSubnets": {
      "Description": "A list of the private subnets",
      "Value": [
        ",",
        [
          "PrivateSubnet1",
          "PrivateSubnet2"
        ]
      ]
    },
    "PublicSubnet1": {
      "Description": "A reference to the public subnet in the 1st Availability Zone",
      "Value": "PublicSubnet1"
    },
    "PublicSubnet2": {
      "Description": "A reference to the public subnet in the 2nd Availability Zone",
      "Value": "PublicSubnet2"
    },
    "PrivateSubnet1": {
      "Description": "A reference to the private subnet in the 1st Availability Zone",
      "Value": "PrivateSubnet1"
    },
    "PrivateSubnet2": {
      "Description": "A reference to the private subnet in the 2nd Availability Zone",
      "Value": "PrivateSubnet2"
    }
  }
}
