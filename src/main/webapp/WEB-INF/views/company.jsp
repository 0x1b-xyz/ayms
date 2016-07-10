<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Companies</title>
</head>
<body>

<div class="row">

    <h2>Companies</h2>
    <ol class="breadcrumb">
        <li><a href="<s:url value="/"/>">Home</a></li>
        <li class="active">Companies</li>
    </ol>
    <div class="table-responsive">
        <caption>table caption</caption>
        <table id="company-tbl" class="table table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Address</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>

</div>

<div class="row">
    <h2>Create</h2>

    <div class="hidden alert alert-danger" role="alert"><strong>Oh snap!</strong> Change a few things up and try
        submitting again.
    </div>

    <form class="form-horizontal" id="company-frm" action="<s:url value="/company"/>" method="post">
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">Company Name</label>
            <div class="col-sm-8">
                <input name="name" id="name" type="text" class="form-control" placeholder="Name">
            </div>
        </div>
        <div class="form-group">
            <label for="address.line1" class="col-sm-2 control-label">Address</label>
            <div class="col-sm-8">
                <input name="address.line1" type="text" class="form-control" id="address.line1" placeholder="Line 1">
            </div>
        </div>
        <div class="form-group">
            <label for="address.line2" class="col-sm-2 control-label"></label>
            <div class="col-sm-8">
                <input name="address.line2" type="text" class="form-control" id="address.line2" placeholder="Line 2">
            </div>
        </div>
        <div class="form-group">
            <label for="address.city" class="col-sm-2 control-label">City</label>
            <div class="col-sm-8">
                <div class="form-group row">
                    <div class="col-sm-5">
                        <input name="address.city" type="text" class="form-control" id="address.city"
                               placeholder="City">
                    </div>
                    <label for="address.state" class="col-sm-1 control-label">State</label>
                    <div class="col-sm-3">
                        <select name="address.state" id="address.state" class="form-control">
                            <option value="AL">Alabama</option>
                            <option value="AK">Alaska</option>
                            <option value="AZ">Arizona</option>
                            <option value="AR">Arkansas</option>
                            <option value="CA">California</option>
                            <option value="CO">Colorado</option>
                            <option value="CT">Connecticut</option>
                            <option value="DE">Delaware</option>
                            <option value="DC">District Of Columbia</option>
                            <option value="FL">Florida</option>
                            <option value="GA">Georgia</option>
                            <option value="HI">Hawaii</option>
                            <option value="ID">Idaho</option>
                            <option value="IL">Illinois</option>
                            <option value="IN">Indiana</option>
                            <option value="IA">Iowa</option>
                            <option value="KS">Kansas</option>
                            <option value="KY">Kentucky</option>
                            <option value="LA">Louisiana</option>
                            <option value="ME">Maine</option>
                            <option value="MD">Maryland</option>
                            <option value="MA">Massachusetts</option>
                            <option value="MI">Michigan</option>
                            <option value="MN">Minnesota</option>
                            <option value="MS">Mississippi</option>
                            <option value="MO">Missouri</option>
                            <option value="MT">Montana</option>
                            <option value="NE">Nebraska</option>
                            <option value="NV">Nevada</option>
                            <option value="NH">New Hampshire</option>
                            <option value="NJ">New Jersey</option>
                            <option value="NM">New Mexico</option>
                            <option value="NY">New York</option>
                            <option value="NC">North Carolina</option>
                            <option value="ND">North Dakota</option>
                            <option value="OH">Ohio</option>
                            <option value="OK">Oklahoma</option>
                            <option value="OR">Oregon</option>
                            <option value="PA">Pennsylvania</option>
                            <option value="RI">Rhode Island</option>
                            <option value="SC">South Carolina</option>
                            <option value="SD">South Dakota</option>
                            <option value="TN">Tennessee</option>
                            <option value="TX">Texas</option>
                            <option value="UT">Utah</option>
                            <option value="VT">Vermont</option>
                            <option value="VA">Virginia</option>
                            <option value="WA">Washington</option>
                            <option value="WV">West Virginia</option>
                            <option value="WI">Wisconsin</option>
                            <option value="WY">Wyoming</option>
                        </select>
                    </div>
                    <label for="address.zipcode" class="col-sm-1 control-label">Zip</label>
                    <div class="col-sm-2">
                        <input name="address.zipcode" type="text" class="form-control" id="address.zipcode"
                               placeholder="Zipcode"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-success">Save</button>
            </div>
        </div>
    </form>

</div>

<script type="text/javascript">
    jQuery(document).ready(function () {

        jQuery('#address\\.zipcode').mask('00000');

        rows('', 'row-Company', '#company-tbl');
        form('#company-frm', 'row-Company', '#company-tbl')

    })
</script>

</body>
</html>
