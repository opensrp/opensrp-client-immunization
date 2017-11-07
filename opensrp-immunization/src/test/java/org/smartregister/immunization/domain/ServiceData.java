package org.smartregister.immunization.domain;

/**
 * Created by real on 23/10/17.
 */

public class ServiceData {

    public static final String services = "[\n" +
            "  {\n" +
            "    \"name\": \"Recurring Services\",\n" +
            "    \"id\": \"Recurring_Services\",\n" +
            "    \"services\": [\n" +
            "      {\n" +
            "        \"type\": \"Vit A\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"Deworming\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"type\": \"ITN\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    public static final String recurringservice = "[\n"+
            "  {\n"+
            "    \"type\": \"Vit A\",\n"+
            "    \"service_logic\": \"\",\n"+
            "    \"units\": \"IU\",\n"+
            "    \"openmrs_service_name\": {\n"+
            "      \"parent_entity\": \"\",\n"+
            "      \"entity\": \"concept\",\n"+
            "      \"entity_id\": \"161534AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n"+
            "    },\n"+
            "    \"openmrs_date\": {\n"+
            "      \"parent_entity\": \"\",\n"+
            "      \"entity\": \"encounter\",\n"+
            "      \"entity_id\": \"encounter_date\"\n"+
            "    },\n"+
            "    \"services\": [\n"+
            "      {\n"+
            "        \"id\": \"10\",\n"+
            "        \"name\": \"Vit A IFC 2\",\n"+
            "        \"dose\": \"50,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+0d\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"11\",\n"+
            "        \"name\": \"Vit A 1\",\n"+
            "        \"dose\": \"100,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"multiple\",\n"+
            "            \"multiple\": {\n"+
            "              \"condition\": \"OR\",\n"+
            "              \"prerequisites\": [\n"+
            "                \"Vit A IFC 2\",\n"+
            "                \"dob\"\n"+
            "              ]\n"+
            "            },\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"12\",\n"+
            "        \"name\": \"Vit A 2\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 1\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"13\",\n"+
            "        \"name\": \"Vit A 3\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 2\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"14\",\n"+
            "        \"name\": \"Vit A 4\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 3\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"15\",\n"+
            "        \"name\": \"Vit A 5\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 4\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"16\",\n"+
            "        \"name\": \"Vit A 6\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 5\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"17\",\n"+
            "        \"name\": \"Vit A 7\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 6\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"18\",\n"+
            "        \"name\": \"Vit A 8\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 7\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"19\",\n"+
            "        \"name\": \"Vit A 9\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 8\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"20\",\n"+
            "        \"name\": \"Vit A 10\",\n"+
            "        \"dose\": \"200,000\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Vit A 9\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      }\n"+
            "    ]\n"+
            "  },\n"+
            "  {\n"+
            "    \"type\": \"Deworming\",\n"+
            "    \"service_logic\": \"\",\n"+
            "    \"units\": \"mg\",\n"+
            "    \"openmrs_service_name\": {\n"+
            "      \"parent_entity\": \"\",\n"+
            "      \"entity\": \"concept\",\n"+
            "      \"entity_id\": \"159922AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n"+
            "    },\n"+
            "    \"openmrs_date\": {\n"+
            "      \"parent_entity\": \"\",\n"+
            "      \"entity\": \"encounter\",\n"+
            "      \"entity_id\": \"encounter_date\"\n"+
            "    },\n"+
            "    \"services\": [\n"+
            "      {\n"+
            "        \"id\": \"1\",\n"+
            "        \"name\": \"Deworming 1\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+12m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"2\",\n"+
            "        \"name\": \"Deworming 2\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 1\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"3\",\n"+
            "        \"name\": \"Deworming 3\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 2\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"4\",\n"+
            "        \"name\": \"Deworming 4\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 3\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"5\",\n"+
            "        \"name\": \"Deworming 5\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 4\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"6\",\n"+
            "        \"name\": \"Deworming 6\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 5\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"7\",\n"+
            "        \"name\": \"Deworming 7\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 6\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"8\",\n"+
            "        \"name\": \"Deworming 8\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 7\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"9\",\n"+
            "        \"name\": \"Deworming 9\",\n"+
            "        \"dose\": \"500\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"Deworming 8\",\n"+
            "            \"offset\": \"+6m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      }\n"+
            "    ]\n"+
            "  },\n"+
            "  {\n"+
            "    \"type\": \"ITN\",\n"+
            "    \"service_logic\": \"\",\n"+
            "    \"units\": \"\",\n"+
            "    \"openmrs_service_name\": {\n"+
            "      \"parent_entity\": \"\",\n"+
            "      \"entity\": \"concept\",\n"+
            "      \"entity_id\": \"159855AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n"+
            "    },\n"+
            "    \"openmrs_date\": {\n"+
            "      \"parent_entity\": \"\",\n"+
            "      \"entity\": \"date\",\n"+
            "      \"entity_id\": \"159432AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n"+
            "    },\n"+
            "    \"services\": [\n"+
            "      {\n"+
            "        \"id\": \"21\",\n"+
            "        \"name\": \"ITN 1\",\n"+
            "        \"dose\": \"\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+0d\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"22\",\n"+
            "        \"name\": \"ITN 2\",\n"+
            "        \"dose\": \"\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"ITN 1\",\n"+
            "            \"offset\": \"+12m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"23\",\n"+
            "        \"name\": \"ITN 3\",\n"+
            "        \"dose\": \"\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"ITN 2\",\n"+
            "            \"offset\": \"+12m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"24\",\n"+
            "        \"name\": \"ITN 4\",\n"+
            "        \"dose\": \"\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"ITN 3\",\n"+
            "            \"offset\": \"+12m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      },\n"+
            "      {\n"+
            "        \"id\": \"25\",\n"+
            "        \"name\": \"ITN 5\",\n"+
            "        \"dose\": \"\",\n"+
            "        \"schedule\": {\n"+
            "          \"due\": {\n"+
            "            \"reference\": \"prerequisite\",\n"+
            "            \"prerequisite\": \"ITN 4\",\n"+
            "            \"offset\": \"+12m\"\n"+
            "          },\n"+
            "          \"expiry\": {\n"+
            "            \"reference\": \"dob\",\n"+
            "            \"offset\": \"+5y\"\n"+
            "          }\n"+
            "        }\n"+
            "      }\n"+
            "    ]\n"+
            "  }\n"+
            "]";
}
