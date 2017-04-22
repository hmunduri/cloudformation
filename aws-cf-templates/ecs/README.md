# ECS Templates for AWS CloudFormation

Find the documentation here: http://templates.cloudonaut.io/en/stable/ecs/

## Developer notes

### ECS RegionMap
To update the region map execute the following lines in your terminal:

```
$ regions=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
$ for region in $regions; do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn-ami-2016.09.g-amazon-ecs-optimized" --query "Images[0].ImageId" --output "text"); printf "'$region':\n  ECSAMI: '$ami'\n"; done
```

## License
All templates are published under Apache License Version 2.0.

## Premium Support
We offer Premium Support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [hello@widdix.de](mailto:hello@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
